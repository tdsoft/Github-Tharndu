package com.android.tdsoft.gpstrackingsys;

/**
 * Created by Admin on 2/1/2016.
 */
public class Constants {
    public static final int NOTIFICATION_ID = 1300;
    public static final int LOCATION_UPDATES_INTERVAL = 10000;
    public static final int LOCATION_UPDATES_FAST_INTERVAL = 5000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 50f;//50 meters
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = (((1000 * 60) * 60) * 10);//ten min
}
