package com.android.tdsoft.gpstrackingsys;

import android.location.Location;
import android.telephony.SmsManager;

import com.google.gson.Gson;

/**
 * Created by Admin on 2/1/2016.
 */
public class AppMessage {

    public AppActions actions;

    public AppMessage(AppActions actions) {
        this.actions = actions;
    }

    public AppMessage() {
        actions = AppActions.DO_NOTHING;
    }

    public AppActions getActions() {
        return actions;
    }

    public enum AppActions {
        DO_NOTHING,
        LOCATION,
        SEND_ONCE,
        SEND_CONTINUOUS,
        STOP_CONTINUOUS
    }

    public double lat;
    public double longi;


    public static void sendMessage(AppActions actions) {
        String json = new Gson().toJson(new AppMessage(actions));
        Utils.sendTextMessage(MyPref.getInstance().getPhoneNumber(),json);
    }

    public static void sendMessage(Location location) {
        if (location == null) {
            return;
        }
        AppMessage locationMessage = new AppMessage();
        locationMessage.lat = location.getLatitude();
        locationMessage.longi = location.getLongitude();
        locationMessage.actions = AppActions.LOCATION;
        String json = new Gson().toJson(locationMessage);
        Utils.sendTextMessage(MyPref.getInstance().getPhoneNumber(),json);

    }


}
