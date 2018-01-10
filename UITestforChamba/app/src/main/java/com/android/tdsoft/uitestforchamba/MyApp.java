package com.android.tdsoft.uitestforchamba;

import android.app.Application;

/**
 * Created by Admin on 2/10/2016.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LocaleHelper.onCreate(this, "en");
    }
}
