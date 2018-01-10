package com.android.tdsoft.gpstrackingsys;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Admin on 2/2/2016.
 */
public class MyPref {
    private static final String PHONE_NUMBER = "pref_phonenumber";
    private static final String ZOOM = "pref_zoom";
    private static final String CONTINUOUS_MARKER = "pref_continuous_marker";
    private static final String GEO_FENCE_ENABLE = "pref_geofence_enable";
    private static final String GEOFENCE_RADIUS = "pref_gefence_radius";

    private static final String LAST_LATI = "pref_lati";
    private static final String LAST_LONGI = "pref_longi";

    private static MyPref instance;

    private SharedPreferences sharedPreferences;

    public MyPref(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static MyPref getInstance() {
        if(instance==null){
            instance = new MyPref(MyApp.getInstance());
        }
        return instance;
    }

    public void setPhoneNumber(String phoneNumber){
        sharedPreferences.edit().putString(PHONE_NUMBER, phoneNumber).commit();
    }

    public String getPhoneNumber(){
        return sharedPreferences.getString(PHONE_NUMBER,"");
    }

    public void setZoom(float zoom){
        sharedPreferences.edit().putFloat(ZOOM, zoom).commit();
    }

    public float getZoom(){
        return sharedPreferences.getFloat(ZOOM, 20f);
    }

    public void setAddContinuousMarker(boolean status){
        sharedPreferences.edit().putBoolean(CONTINUOUS_MARKER, status).commit();
    }

    public boolean isAddContinuousMarker(){
        return sharedPreferences.getBoolean(CONTINUOUS_MARKER, true);
    }


    public void setGeoFenceEnable(boolean status){
        sharedPreferences.edit().putBoolean(GEO_FENCE_ENABLE, status).commit();
    }

    public boolean isGeoFenceEnable() {
        return sharedPreferences.getBoolean(GEO_FENCE_ENABLE, true);
    }

    public void setGeofenceRadius(float radiusMeters){
        sharedPreferences.edit().putFloat(GEOFENCE_RADIUS, radiusMeters).commit();
    }

    public float getGeoFenceRadius() {
        return sharedPreferences.getFloat(GEOFENCE_RADIUS, 50f);
    }

    public void saveVeryLastLocation(AppMessage appMessage){
        sharedPreferences.edit().putFloat(LAST_LATI, (float) appMessage.lat).commit();
        sharedPreferences.edit().putFloat(LAST_LONGI, (float) appMessage.longi).commit();
    }
    public AppMessage getLastLocation(){
        AppMessage appMessage = new AppMessage();
        appMessage.actions = AppMessage.AppActions.LOCATION;
        appMessage.lat = sharedPreferences.getFloat(LAST_LATI, -1f);
        appMessage.longi = sharedPreferences.getFloat(LAST_LONGI, -1f);
        return appMessage;
    }

}
