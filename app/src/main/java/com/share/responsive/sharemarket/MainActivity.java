package com.share.responsive.sharemarket;

import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
AutoCompleteTextView autoCompleteTextView;
String[] country_names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.country);
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
        Button getquote = (Button)findViewById(R.id.getquote);
        getquote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DetailsActivity.class);
                startActivity(intent);
            }
        });

    }
}
