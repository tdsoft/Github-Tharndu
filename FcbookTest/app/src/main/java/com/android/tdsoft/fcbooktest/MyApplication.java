package com.android.tdsoft.fcbooktest;

import android.app.Application;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;

/**
 * Created by Admin on 1/4/2016.
 */
public class MyApplication extends Application {

    private static MyApplication instance;
    private CallbackManager callbackManager;

    public static MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        if (BuildConfig.DEBUG) {
            FacebookSdk.setIsDebugEnabled(true);
            //FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        }
    }

    public CallbackManager getCallbackManager() {
        return callbackManager;
    }
}
