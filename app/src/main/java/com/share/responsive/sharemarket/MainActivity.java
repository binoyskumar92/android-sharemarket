package com.share.responsive.sharemarket;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
AutoCompleteTextView actv;
String selectedSymbol="";
String[] country_names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    }
}
