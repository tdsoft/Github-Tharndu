package com.tdsoft.whereareyoudude.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tdsoft.whereareyoudude.MyApp;
import com.tdsoft.whereareyoudude.smack.data.User;

/**
 * Created by Admin on 7/21/2016.
 */
public class PreferenceHandler {
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static PreferenceHandler INSTANCE = null;
    public SharedPreferences shardPref;
    public static PreferenceHandler getInstance(){
        if(INSTANCE == null){
            INSTANCE = new PreferenceHandler();
        }
        INSTANCE.shardPref = PreferenceManager.getDefaultSharedPreferences(MyApp.getInstance());
        return INSTANCE;
    }

    public void saveUserNameAndPass(User user){
        shardPref.edit().putString(USERNAME,user.getUserName()).putString(PASSWORD,user.getPassword()).apply();
    }

    public User getCurrentUser(){
        User user = null;
        String userName = shardPref.getString(USERNAME, null);
        String password = shardPref.getString(PASSWORD, null);
        if((userName != null && userName.length() > 0) && (password !=null && password.length() > 0)){
            user = new User();
            user.setUserName(userName);
            user.setPassword(password);
        }
        return user;
    }

    public void logout(){
        shardPref.edit().putString(USERNAME,null).putString(PASSWORD,null).apply();
    }
}
