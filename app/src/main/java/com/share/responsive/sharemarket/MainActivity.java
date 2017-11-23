package com.share.responsive.sharemarket;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
AutoCompleteTextView actv;
String selectedSymbol="";
String[] country_names;
ListView favlistview;
boolean isFavListLoaded=false;
ArrayList<FavoritesInfo> favInfoFromPreference;
    ArrayAdapter<FavoritesInfo> favListAdapter;
    @Override
    protected void onStart() {
        super.onStart();
        if(isFavListLoaded) {
            favInfoFromPreference = UIUtils.getAllItemsfromSharedPreference();
            favlistview.setAdapter(new CustomFavListAdapter());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialise sharedpreferences
        UIUtils.initialiseSharedPreference(getApplicationContext());

        actv = (AutoCompleteTextView) findViewById(R.id.country);
        actv.setThreshold(1);
        String[] from = { "name" };
        int[] to = { android.R.id.text1};
        SimpleCursorAdapter a = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, null, from, to, 0);
        a.setStringConversionColumn(1);
        FilterQueryProvider provider = new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                if (constraint == null) {
                    return null;
                }
                String[] columnNames = { BaseColumns._ID, "name", "description" };
                MatrixCursor c = new MatrixCursor(columnNames);
                try {
                    String urlString ="http://dev.markitondemand.com/MODApis/Api/v2/Lookup/json?input="+constraint;
                    URL url = new URL(urlString);
                    InputStream stream = url.openStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                    String jsonStr = reader.readLine();
                    JSONArray json = new JSONArray(jsonStr);
                    for(int i=0;i<json.length();i++){
                        c.newRow().add(i).add(json.getJSONObject(i).getString("Symbol")+" - "+json.getJSONObject(i).getString("Name")+" ("+json.getJSONObject(i).getString("Exchange")+")");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return c;
            }
        };
        a.setFilterQueryProvider(provider);
        actv.setAdapter(a);
       actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
               MatrixCursor matrix =  (MatrixCursor)parent.getItemAtPosition(position);
               selectedSymbol = matrix.getString(1);
               selectedSymbol = selectedSymbol.split("-")[0].trim();
           }
       });

        Button getquote = (Button)findViewById(R.id.getquote);
        getquote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedSymbol != "") {
                    Bundle bundle = new Bundle();
                    bundle.putString("symbol", selectedSymbol);
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"Please select a valid symbol",Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button clear = (Button)findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actv.setText("");
                selectedSymbol="";
            }
        });
        //Favorite Listview
        favlistview=(ListView)findViewById(R.id.favoriteslist);
        if(!isFavListLoaded) {
            favInfoFromPreference = UIUtils.getAllItemsfromSharedPreference();
            favListAdapter = new CustomFavListAdapter();
            favlistview.setAdapter(favListAdapter);
            isFavListLoaded=true;
        }
    }
    private class CustomFavListAdapter extends ArrayAdapter<FavoritesInfo> {
        public CustomFavListAdapter() {
                super(MainActivity.this, R.layout.favlist, favInfoFromPreference);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View itemView=convertView;
            if(itemView==null){
                itemView=getLayoutInflater().inflate(R.layout.favlist,parent,false);
            }
            FavoritesInfo favinfo = favInfoFromPreference.get(position);
            TextView symbol=(TextView)itemView.findViewById(R.id.favsymbol);
            symbol.setText(favinfo.getSymbol());
            TextView price=(TextView)itemView.findViewById(R.id.favprice);
            price.setText(favinfo.getPrice());
            TextView change=(TextView)itemView.findViewById(R.id.favchange);
            change.setText(favinfo.getChange()+" ("+favinfo.getChangeperc()+"%)");
            if(Float.parseFloat(favinfo.getChange())<0){
                change.setTextColor(Color.RED);
            }else{
                change.setTextColor(Color.GREEN);
            }
            return itemView;
        }
    }
}
