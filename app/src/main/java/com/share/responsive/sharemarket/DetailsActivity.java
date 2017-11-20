package com.share.responsive.sharemarket;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private SectionsPageAdapter sectionPageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        viewPager = (ViewPager)findViewById(R.id.container);
        setupViewPager(viewPager);
        TabLayout tabLayout=(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter =new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(),"Current");
        adapter.addFragment(new Tab2Fragment(),"Historical");
        adapter.addFragment(new Tab3Fragment(),"News");
        viewPager.setAdapter(adapter);
    }


}
