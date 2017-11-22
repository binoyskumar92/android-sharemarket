package com.share.responsive.sharemarket;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;

/**
 * Created by Binoy on 11/20/2017.
 */

public class Tab3Fragment extends Fragment {
    private static final String TAG = "Tab3Fragment";
    private static final String NEWS_URL = "http://seekingalpha.com/api/sa/combined/";
    ListView newsList;
    String symbol;
    JSONObject newsData;
    ArrayList<NewsFeed> newsfeed;
    boolean isNewsDataRequested = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!isNewsDataRequested) {
            requestNewsData();
            isNewsDataRequested = true;
        }
    }

    private void requestNewsData() {
        //News
        if (symbol == null) {
            symbol = getArguments().getString("symbol");
        }
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest newsRequest = new StringRequest(Request.Method.GET, NEWS_URL + symbol,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            XmlToJson xmlToJson = new XmlToJson.Builder(response).build();
                            newsData = xmlToJson.toJson();
                            parseJsonNews(newsData);
                        } catch (Exception e) {
                            e.printStackTrace();
                            ;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: News" + error.getMessage());
                        Toast.makeText(getActivity(), "Server error. Please try again later.", Toast.LENGTH_SHORT).show();

                    }
                }
        );
        queue.add(newsRequest);
    }

    private void parseJsonNews(JSONObject newsData) {
        try {
            newsfeed = new ArrayList<NewsFeed>();
            JSONArray newsContent = newsData.getJSONObject("rss").getJSONObject("channel").getJSONArray("item");
            for (int i = 0; i < 5; i++) {

                newsfeed.add(new NewsFeed(newsContent.getJSONObject(i).getString("title"),
                        newsContent.getJSONObject(i).getString("sa:author_name"),
                        newsContent.getJSONObject(i).getString("pubDate"),
                        newsContent.getJSONObject(i).getString("link")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab3_fragment, container, false);
        symbol = getArguments().getString("symbol");
        newsList = (ListView) view.findViewById(R.id.news);

        return view;
    }

}
