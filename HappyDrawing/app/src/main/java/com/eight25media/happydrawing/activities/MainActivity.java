package com.eight25media.happydrawing.activities;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.eight25media.happydrawing.R;
import com.eight25media.happydrawing.callback.OnImageTakenListener;
import com.eight25media.happydrawing.databinding.ActivityMainBinding;
import com.eight25media.happydrawing.fragments.CameraFragment;
import com.eight25media.happydrawing.fragments.DrawingFragment;

import java.io.File;

public class MainActivity extends AppCompatActivity implements OnImageTakenListener{

    public ActivityMainBinding mActivityMainBinding;
    private MyPagerAdapter myPagerAdapter;

    private String mTagDrawingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mActivityMainBinding =  DataBindingUtil.setContentView(this, R.layout.activity_main);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        ViewPager viewPager = mActivityMainBinding.pagerContainer.vpager;

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public int mPos;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(state==ViewPager.SCROLL_STATE_IDLE && mPos == 1) {
                    DrawingFragment drawingFragment = (DrawingFragment) getSupportFragmentManager().findFragmentByTag(mTagDrawingFragment);
                    drawingFragment.refresh();
                }
            }
        });
    }



    @Override
    public void onImageTaken(File bitmap) {
        DrawingFragment drawingFragment = (DrawingFragment) getSupportFragmentManager().findFragmentByTag(mTagDrawingFragment);
        drawingFragment.setBitmap(bitmap);
        mActivityMainBinding.pagerContainer.vpager.setCurrentItem(1);

    }

    @Override
    public void onImageTaken(Bitmap bitmap) {
        DrawingFragment drawingFragment = (DrawingFragment) getSupportFragmentManager().findFragmentByTag(mTagDrawingFragment);
        drawingFragment.setBitmap(bitmap);
        mActivityMainBinding.pagerContainer.vpager.setCurrentItem(1);
    }

    public void setTagDrawingFragment(String mTagDrawingFragment) {
        this.mTagDrawingFragment = mTagDrawingFragment;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case 0:
                    fragment = new CameraFragment();

                    break;
                case 1:
                    fragment = new DrawingFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
