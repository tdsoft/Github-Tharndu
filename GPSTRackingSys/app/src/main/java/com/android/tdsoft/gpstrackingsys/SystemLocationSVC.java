package com.android.tdsoft.gpstrackingsys;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Date;

import de.greenrobot.event.EventBus;

public class SystemLocationSVC extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<Status> {
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates;
    private String mLastUpdateTime;
    private Location mCurrentLocation;
    private EventBus eventBus;
    private PendingIntent mGeofencePendingIntent;
    private AppMessage appMessage;


    public SystemLocationSVC() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        eventBus = EventBus.getDefault();
        // Create an instance of GoogleAPIClient.
        createLocationRequest();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mGoogleApiClient.connect();
        startForeground();
        return START_STICKY;
    }

    public void onEvent(AppMessage appMessage) {
        if (appMessage != null) {
            System.out.println("Received " + new Gson().toJson(appMessage));
            this.appMessage = appMessage;
            if(appMessage.getActions()== AppMessage.AppActions.LOCATION){

                MyPref.getInstance().saveVeryLastLocation(appMessage);

                if(MyPref.getInstance().isGeoFenceEnable()){
                    stopGeoFencing();
                    Utils.clearGeoFenceList();
                    Utils.addFenceList(appMessage);
                    try {
                        LocationServices.GeofencingApi.addGeofences(
                                mGoogleApiClient,
                                // The GeofenceRequest object.
                                getGeofencingRequest(),
                                // A pending intent that that is reused when calling removeGeofences(). This
                                // pending intent is used to generate an intent when a matched geofence
                                // transition is observed.
                                getGeofencePendingIntent()
                        ).setResultCallback(this); // Result processed in onResult().
                    } catch (SecurityException securityException) {
                        securityException.printStackTrace();
                    }

                }
            }
        }
    }

    @Override
    public void onDestroy() {
        stopLocationUpdates();
        stopGeoFencing();
        mGoogleApiClient.disconnect();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Constants.LOCATION_UPDATES_INTERVAL); //20 sec
        mLocationRequest.setFastestInterval(Constants.LOCATION_UPDATES_FAST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    protected void startLocationUpdates() throws SecurityException{
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("Error");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        try {
            if (connectionResult != null)
                System.out.println(connectionResult.getErrorMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI() {
        if (mCurrentLocation != null) {
            Log.d("SystemLocationSVC", mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude() + ", accuracy: " + mCurrentLocation.getAccuracy());
            eventBus.post(mCurrentLocation);

            if(appMessage != null) {
                switch (appMessage.getActions()) {
                    case SEND_CONTINUOUS:
                        AppMessage.sendMessage(mCurrentLocation);
                        break;
                    case SEND_ONCE:
                        AppMessage.sendMessage(mCurrentLocation);
                        appMessage = null;
                        break;
                    case STOP_CONTINUOUS:
                        appMessage = null;
                        break;
                    default:
                }
            }
        }
    }

    private void startForeground() {
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Intent i = new Intent(this, LoginActivity.class);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.title))
                .setTicker(getString(R.string.title))
                .setContentText("Android System updates")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                .setContentIntent(pendingIntent)
                .setOngoing(true).build();


        startForeground(Constants.NOTIFICATION_ID, notification);
    }

    private void stopGeoFencing(){
        LocationServices.GeofencingApi.removeGeofences(mGoogleApiClient,getGeofencePendingIntent()).setResultCallback(this); // Result processed in onResult().
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @Override
    public void onResult(Status status) {
        if (status.isSuccess()) {
            Log.d("Geofence", "Removing|Adding is Success");
        }else {
            Log.e("Geofence", "Removing|Adding is failed");
        }
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(Utils.getGeoFenceList());
        return builder.build();
    }

}
