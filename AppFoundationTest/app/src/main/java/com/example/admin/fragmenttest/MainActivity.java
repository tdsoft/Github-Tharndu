package com.example.admin.fragmenttest;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import com.android.tdsoft.androidappfoundation.base.AppBaseActivity;

public class MainActivity extends AppBaseActivity {

    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().add(new BlankFragment(),"Blank").commit();
//        this.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
//        registerCoordinatorLayout(coordinatorLayout);
    }

}
