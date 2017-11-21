package com.share.responsive.sharemarket;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = "DetailsActivity";
    private ViewPager viewPager;
    private SectionsPageAdapter sectionPageAdapter;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        bundle = getIntent().getExtras();
        viewPager = (ViewPager)findViewById(R.id.container);
        setupViewPager(viewPager);
        TabLayout tabLayout=(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //request stock data
        requestStockData(bundle.getString("symbol"));
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter =new SectionsPageAdapter(getSupportFragmentManager());
        Fragment f1 = new Tab1Fragment();
        f1.setArguments(bundle);
        Fragment f2 = new Tab2Fragment();
        f2.setArguments(bundle);
        Fragment f3 = new Tab3Fragment();
        f3.setArguments(bundle);
        adapter.addFragment(f1,"Current");
        adapter.addFragment(f2,"Historical");
        adapter.addFragment(f3,"News");
        viewPager.setAdapter(adapter);
    }

    private void requestStockData(String symbol){

        RequestQueue queue = Volley.newRequestQueue(DetailsActivity.this);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, "http://sharemarkethw-env.us-east-1.elasticbeanstalk.com/timeseries?symbol="+symbol, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                EventBus.getDefault().post(new StockDataReceivedEvent(response.toString()));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error);
            }
        });
        getRequest.setRetryPolicy(new DefaultRetryPolicy(
                8000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(getRequest);
    }


}
