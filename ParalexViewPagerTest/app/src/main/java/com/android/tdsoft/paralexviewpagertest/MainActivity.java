package com.android.tdsoft.paralexviewpagertest;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private MyPagerAdapter myPageAdaper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPageAdaper = new MyPagerAdapter(this);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        parallaxItems[0] = new ParallaxItem(R.id.centerImage,1.5f);
        parallaxItems[1] = new ParallaxItem(R.id.text,0.8f);

        mViewPager.setPageTransformer(false,new MyParallaxPagerTransformer(parallaxItems));
        mViewPager.setAdapter(myPageAdaper);
    }
}
