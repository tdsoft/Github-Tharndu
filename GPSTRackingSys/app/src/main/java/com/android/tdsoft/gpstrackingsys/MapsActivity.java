package com.android.tdsoft.gpstrackingsys;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import de.greenrobot.event.EventBus;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSIONS_CODE = 1;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_PERMISSIONS_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int grantResult = grantResults[i];

                if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResult == PackageManager.PERMISSION_GRANTED) {
                        startService();
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS},
                                LOCATION_PERMISSIONS_CODE);
                    }
                }
            }
        }
    }

    private void startService() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS},
                    LOCATION_PERMISSIONS_CODE);
            return;
        } else {
            Intent intent = new Intent(this, SystemLocationSVC.class);
            startService(intent);
        }
    }


    private Marker mMarker;

    public void onEvent(AppMessage locationMessage) {
        if (locationMessage != null && locationMessage.getActions() == AppMessage.AppActions.LOCATION) {
            System.out.println(locationMessage.lat + ", " + locationMessage.longi);
            LatLng sydney = new LatLng(locationMessage.lat, locationMessage.longi);
            if(MyPref.getInstance().isAddContinuousMarker() && mMarker!=null){
                mMarker.remove();
            }
            mMarker = mMap.addMarker(new MarkerOptions().position(sydney).title("Other person"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, MyPref.getInstance().getZoom()));
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        //EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);


        if(MyPref.getInstance().getLastLocation().lat > -1 && MyPref.getInstance().getLastLocation().longi > -1){
            EventBus.getDefault().post(MyPref.getInstance().getLastLocation());
        }

        startService();
    }

    public void sendOnceMessage(View view) {
        AppMessage.sendMessage(AppMessage.AppActions.SEND_ONCE);
    }

    public void sendContinuousMessage(View view) {
        AppMessage.sendMessage(AppMessage.AppActions.SEND_CONTINUOUS);
    }

    public void onStopClick(View view) {
        AppMessage.sendMessage(AppMessage.AppActions.STOP_CONTINUOUS);
    }

}
