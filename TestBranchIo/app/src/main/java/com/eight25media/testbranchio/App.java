package com.eight25media.testbranchio;

import android.app.Application;

import io.branch.referral.Branch;

/**
 * Created by Admin on 2/24/2016.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Automatic session tracking
        Branch.getAutoInstance(this);
    }
}
