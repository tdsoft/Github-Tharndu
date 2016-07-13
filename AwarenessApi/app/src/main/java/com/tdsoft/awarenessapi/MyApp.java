package com.tdsoft.awarenessapi;

import android.app.Application;
import android.widget.TabHost;

import com.tdsoft.awarenessapi.googlehelper.GoogleClientController;

/**
 * Created by Admin on 7/12/2016.
 */
public class MyApp extends Application {
    private static MyApp INSTANCE;

    public static MyApp getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        GoogleClientController.getInstance().init(this);
    }
}
