package com.example.testapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.testapp.R;
import com.example.testapp.adapter.ViewPagerAdapterHome;
import com.example.testapp.adapter.ViewPagerAdapterHomeServiser;
import com.google.android.material.tabs.TabLayout;

public class HomeActivityServiser extends AppCompatActivity {

    TabLayout mTabLayout;

    ViewPager2 mViewPager;
    ViewPagerAdapterHomeServiser adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_serviser);

        //VIEW PAGER
        mViewPager = findViewById(R.id.viewPagerHome2);

        //TAB LAYOUT
        mTabLayout = findViewById(R.id.tabLayoutServiser);

        adapter = new ViewPagerAdapterHomeServiser(getSupportFragmentManager(), getLifecycle());
        mViewPager.setAdapter(adapter);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                mTabLayout.selectTab(mTabLayout.getTabAt(position));
            }
        });

    }
}