package com.android.tdsoft.gpstrackingsys;

import android.app.Application;

/**
 * Created by Admin on 2/2/2016.
 */
public class MyApp extends Application {
    private static MyApp instance;

    public static MyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
