package com.android.tdsoft.gpstrackingsys;

import android.telephony.SmsManager;

import com.google.android.gms.location.Geofence;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2/2/2016.
 */
public class Utils {
    public static void sendTextMessage(String phoneNumber, String message){
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(MyPref.getInstance().getPhoneNumber(), null, message, null, null);
    }

    private static List<Geofence> geoFenceList = new ArrayList<>();
    private static int requestId;
    public static void clearGeoFenceList(){
        geoFenceList.clear();
    }
    public static void addFenceList(AppMessage appMessage){
        requestId++;
        geoFenceList.add(new Geofence.Builder()
                .setRequestId("id" + requestId)
                .setCircularRegion(appMessage.lat, appMessage.longi, MyPref.getInstance().getGeoFenceRadius())
                .setExpirationDuration(Constants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
    }

    public static List<Geofence> getGeoFenceList(){
        return geoFenceList;
    }
}
