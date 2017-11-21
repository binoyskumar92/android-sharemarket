package com.share.responsive.sharemarket;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

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


}
