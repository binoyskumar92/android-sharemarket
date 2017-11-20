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
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Binoy on 11/20/2017.
 */

public class Tab1Fragment extends Fragment{
    ImageButton fb,favorties;
    boolean isFavClicked=false;
    ArrayList<StockData> stockData=new ArrayList<StockData>();
    ListView stocklist;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.tab1_fragment,container,false);
       fb=(ImageButton)view.findViewById(R.id.fb);
       favorties=(ImageButton)view.findViewById(R.id.favorites);
        stocklist=(ListView)view.findViewById(R.id.stocklist);
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
        populateList();
        initializeList();
        return view;
    }
    public Activity getCurrentActivity(){
        return getActivity();
    }
    private void initializeList() {
        ArrayAdapter<StockData> customAdapter = new CustomAdapter();
        stocklist.setAdapter(customAdapter);
        UIUtils.setListViewHeightBasedOnItems(stocklist);
    }

    private void populateList() {
        if(stockData.isEmpty()) {
            stockData.add(new StockData("Price", "120.2", R.drawable.downarrow));
            stockData.add(new StockData("LastPrice", "110.2", R.drawable.downarrow));
            stockData.add(new StockData("Change", "0.24", R.drawable.uparrow));
            stockData.add(new StockData("ChangePercent", "0.8", R.drawable.uparrow));

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
            imageView.setImageResource(currStockData.getArrow());
            TextView parameter=(TextView)itemView.findViewById(R.id.parameter);
            parameter.setText(currStockData.getParameter());
            TextView value=(TextView)itemView.findViewById(R.id.value);
            value.setText(currStockData.getValue());
            return itemView;
        }
    }
}
