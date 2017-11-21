package com.share.responsive.sharemarket;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import de.greenrobot.event.EventBus;

/**
 * Created by Binoy on 11/20/2017.
 */

public class Tab1Fragment extends Fragment{
    private static final String TAG = "Tab1Fragment";
    ImageButton fb,favorties;
    boolean isFavClicked=false;
    StockTable stockTable;
    ArrayList<StockData> stockData=new ArrayList<StockData>();
    ListView stocklist;
    String symbol="";
    ProgressBar pgbStockData;
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.tab1_fragment,container,false);
       fb=(ImageButton)view.findViewById(R.id.fb);
       favorties=(ImageButton)view.findViewById(R.id.favorites);
        stocklist=(ListView)view.findViewById(R.id.stocklist);
        pgbStockData=(ProgressBar)view.findViewById(R.id.stockProgressBar);
        symbol=getArguments().getString("symbol");
       favorties.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(!isFavClicked){
               favorties.setImageResource(R.drawable.filled);
               isFavClicked=true;
               }else{
                   favorties.setImageResource(R.drawable.empty);
                   isFavClicked=false;
               }
           }
       });
        return view;
    }
    public Activity getCurrentActivity(){
        return getActivity();
    }
    private void initializeList() {
        pgbStockData.setVisibility(View.INVISIBLE);
        ArrayAdapter<StockData> customAdapter = new CustomAdapter();
        stocklist.setAdapter(customAdapter);
        UIUtils.setListViewHeightBasedOnItems(stocklist);
    }

    private void populateList() {
        if(stockData.isEmpty()) {
            stockData.add(new StockData("Stock Symbol", stockTable.getStockSymbol(), R.drawable.downarrow));
            stockData.add(new StockData("Last Price", stockTable.getLastPrice(), R.drawable.downarrow));
            stockData.add(new StockData("Change", stockTable.getChange()+" ("+stockTable.getChangeperc()+")", R.drawable.uparrow));
            stockData.add(new StockData("Timestamp", stockTable.getTimestamp(), R.drawable.uparrow));
            stockData.add(new StockData("Open", stockTable.getOpen(), R.drawable.uparrow));
            stockData.add(new StockData("Close", stockTable.getClose(), R.drawable.uparrow));
            stockData.add(new StockData("Day's Range", stockTable.getDayRange(), R.drawable.uparrow));
            stockData.add(new StockData("Volume", stockTable.getVolume(), R.drawable.uparrow));
        }
    }

    public void onEvent(StockDataReceivedEvent event) {
        Toast.makeText(getActivity(),"Data Received from eventbus", Toast.LENGTH_LONG).show();
        parseData(event.stockData);

        populateList();
        initializeList();
    }

    private float roundTwoDecimals(float d) {
        DecimalFormat twoDForm = new DecimalFormat("0.00");
        return Float.valueOf(twoDForm.format(d));
    }
    public boolean isHoliday(Date date)
    {
        boolean isHoliday = false;

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
        {
            isHoliday = true;
        }
        return isHoliday;
    }
    private void parseData(String stockData) {
        String sMeta = "Meta Data";
        String sTimeSeries = "Time Series (Daily)";
        stockTable=new StockTable();
        int i=0;
        try {
            JSONObject json = new JSONObject(stockData);
            JSONArray jsonArray = json.getJSONObject("Time Series (Daily)").names();
            JSONObject jsonObject = json.getJSONObject("Time Series (Daily)");

           // String openPrice = jsonObject.getJSONObject(jsonArray.getString(0)).getString("1. open");
            stockTable.setStockSymbol(symbol);
            float lastPrice = roundTwoDecimals(Float.parseFloat(jsonObject.getJSONObject(jsonArray.getString(0)).getString("4. close")));
            stockTable.setLastPrice(lastPrice+"");
            float change = Float.parseFloat(jsonObject.getJSONObject(jsonArray.getString(0)).getString("4. close")) - Float.parseFloat(jsonObject.getJSONObject(jsonArray.getString(1)).getString("4. close"));
            stockTable.setChange(roundTwoDecimals(change)+"");
            float changeperc = (change/Float.parseFloat(jsonObject.getJSONObject(jsonArray.getString(1)).getString("4. close")))*100;
            stockTable.setChangeperc(roundTwoDecimals(changeperc)+"");
            float open = Float.parseFloat(jsonObject.getJSONObject(jsonArray.getString(0)).getString("1. open"));
            stockTable.setOpen(roundTwoDecimals(open)+"");
            String range = roundTwoDecimals(Float.parseFloat(jsonObject.getJSONObject(jsonArray.getString(0)).getString("3. low"))) + "-" + roundTwoDecimals(Float.parseFloat(jsonObject.getJSONObject(jsonArray.getString(0)).getString("2. high")));
            stockTable.setDayRange(range);
            String volume = jsonObject.getJSONObject(jsonArray.getString(0)).getString("5. volume");
            stockTable.setVolume(volume);

            //Time
            setTimeStampNClose(jsonArray.getString(0),jsonArray.getString(1),jsonObject.getJSONObject(jsonArray.getString(1)).getString("4. close"),jsonObject.getJSONObject(jsonArray.getString(0)).getString("4. close"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        // String dCurrDate = Object.keys(oStockData["Time Series (Daily)"])[0];
       // var dPrevDate = Object.keys(oStockData["Time Series (Daily)"])[1];
    }


    private void setTimeStampNClose(String currDate, String prevDate, String prevClose, String currClose) {
        String timestamp,close,currentDateandTime="";
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        boolean isHolidayToday = isHoliday(new Date());
        Date parsed = null;
        try {
             currentDateandTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
            parsed = format.parse(currentDateandTime);

        TimeZone tzz = TimeZone.getTimeZone("America/New_York");
        format.setTimeZone(tzz);
        String newYorkCurrentTime = format.format(parsed);
        if(isHolidayToday){
            timestamp = currDate + " 16:00:00" + " EST";
            close = currClose;
        }else{
            Calendar cal = Calendar.getInstance();
            cal.setTime(new SimpleDateFormat("HH:mm:ss").parse(newYorkCurrentTime));
            int hour = cal.get(Calendar.HOUR_OF_DAY); //Get the hour from the calendar
            if(hour <= 16 && hour >= 9)              // Check if hour is between 8 am and 11pm
            {
                timestamp = currDate + ' ' +newYorkCurrentTime+" "+tzz.getDisplayName(false,TimeZone.SHORT);
                close = prevClose;
            }else{
                timestamp = currDate + " 16:00:00 "+tzz.getDisplayName(false,TimeZone.SHORT);
                close = currClose;
            }
            stockTable.setTimestamp(timestamp);
            stockTable.setClose(close);
        }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private class CustomAdapter extends ArrayAdapter<StockData> {
        public CustomAdapter() {
            super(getCurrentActivity(),R.layout.stocklist,stockData);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView=convertView;
            if(itemView==null){
                itemView=getLayoutInflater().inflate(R.layout.stocklist,parent,false);
            }
            StockData currStockData=stockData.get(position);
//            return super.getView(position, convertView, parent);
            ImageView imageView = (ImageView)   itemView.findViewById(R.id.arrow);

            if(position!=2){
                imageView.setVisibility(View.INVISIBLE);
            }else{
                if(Float.parseFloat(currStockData.getValue().split(" ")[0].trim())<0) {
                    imageView.setImageResource(R.drawable.downarrow);
                }else{
                    imageView.setImageResource(R.drawable.uparrow);
                }
            }
            TextView parameter=(TextView)itemView.findViewById(R.id.parameter);
            parameter.setText(currStockData.getParameter());
            TextView value=(TextView)itemView.findViewById(R.id.value);
            value.setText(currStockData.getValue());
            return itemView;
        }
    }
}
