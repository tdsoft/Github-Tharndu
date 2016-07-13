package com.tdsoft.movecarmarker;

import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    final Handler mhandler = new Handler();
    int i = 1;
    List<LatLng> latLngs = new ArrayList<>();
    private LatLng lastLocation;
    private Marker marker;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latLngs.add(new LatLng(6.915842, 79.876414));
        latLngs.add(new LatLng(6.916311, 79.875971));
        latLngs.add(new LatLng(6.916646, 79.875692));
        latLngs.add(new LatLng(6.916782, 79.875960));
        latLngs.add(new LatLng(6.916803, 79.876269));

        latLngs.add(new LatLng(6.916827 , 79.876387));
        latLngs.add(new LatLng(6.916835 , 79.876497));
        latLngs.add(new LatLng(6.916856 , 79.876706));
        latLngs.add(new LatLng(6.916854 , 79.876899));
        latLngs.add(new LatLng(6.916880 , 79.877103));
        latLngs.add(new LatLng(6.916894 , 79.877299));

        latLngs.add(new LatLng(6.916888 , 79.877404));
        latLngs.add(new LatLng(6.916888 , 79.877508));
        latLngs.add(new LatLng(6.916886 , 79.877594));
        latLngs.add(new LatLng(6.916963 , 79.877656));
        latLngs.add(new LatLng(6.917179 , 79.87765));
        latLngs.add(new LatLng(6.917517 , 79.877594));


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngs.get(0), 15));
        marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.car)).position(latLngs.get(0)).flat(true));
        lastLocation = latLngs.get(0);

    }

    private void startMoving() {
        runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    if (i < latLngs.size()) {

                        animateMarker(marker, latLngs.get(i), bearingBetweenLatLngs(lastLocation, latLngs.get(i)), false);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                } finally {
                    i++;
                    //also call the same runnable to call it at regular interval
                    mhandler.postDelayed(this, 1000);
                }
            }
        };
        mhandler.postDelayed(runnable, 1000);
    }

    public void animateMarker(final Marker marker, final LatLng toPosition, final float rotation, final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * toPosition.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                marker.setRotation(rotation);
                lastLocation = toPosition;
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    private float bearingBetweenLatLngs(LatLng beginLatLng,LatLng endLatLng) {
        Location beginLocation = convertLatLngToLocation(beginLatLng);
        Location endLocation = convertLatLngToLocation(endLatLng);
        return beginLocation.bearingTo(endLocation);
    }

    private Location convertLatLngToLocation(LatLng latLng) {
        Location location = new Location("someLoc");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }

    public void onClick(View view) {
        i = 0;
        mhandler.removeCallbacks(runnable);
        startMoving();
    }
}
