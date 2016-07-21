package com.tdsoft.whereareyoudude;

import android.app.Application;

import com.tdsoft.whereareyoudude.smack.ConnectionManager;

import org.jxmpp.stringprep.XmppStringprepException;

/**
 * Created by Admin on 7/14/2016.
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
        try {
            ConnectionManager.getInstance().init(this);
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
    }
}
