package com.example.testapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.example.testapp.R;
import com.example.testapp.adapter.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    ViewPager2 mViewPager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //VIEW PAGER
        mViewPager = findViewById(R.id.viewPager2);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        mViewPager.setAdapter(adapter);

    }
}