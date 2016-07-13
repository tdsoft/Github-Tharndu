package com.android.tdsoft.testbncio;

import android.app.Application;

import io.branch.referral.Branch;

/**
 * Created by Admin on 5/27/2016.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Automatic session tracking
        Branch.getAutoInstance(this);
    }
}
