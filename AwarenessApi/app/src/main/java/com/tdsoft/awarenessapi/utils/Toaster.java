package com.tdsoft.awarenessapi.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.tdsoft.awarenessapi.MyApp;
import com.tdsoft.awarenessapi.R;

/**
 * Created by Admin on 7/12/2016.
 */
public class Toaster {
    public static void showShortMsg(@StringRes int msg){
        Toast.makeText(MyApp.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showShortMsg(String msg){
        Toast.makeText(MyApp.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
}
