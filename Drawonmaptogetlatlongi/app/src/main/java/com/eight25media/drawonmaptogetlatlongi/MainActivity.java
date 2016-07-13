package com.eight25media.drawonmaptogetlatlongi;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private static final String TAG = "polygon";
    private GoogleMap mGoogleMap;
    private View mMapShelterView;
    private GestureDetector mGestureDetector;
    private ArrayList<LatLng> mLatlngs = new ArrayList<LatLng>();
    private PolylineOptions mPolylineOptions;
    private PolygonOptions mPolygonOptions;
    // flag to differentiate whether user is touching to draw or not
    private boolean mDrawFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMapShelterView = (View) findViewById(R.id.drawer_view);
        mGestureDetector = new GestureDetector(this, new GestureListener());
        mMapShelterView.setOnTouchListener(this);
        initilizeMap();
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return false;
        }
    }

    /**
     * Ontouch event will draw poly line along the touch points
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int X1 = (int) event.getX();
        int Y1 = (int) event.getY();
        Point point = new Point();
        point.x = X1;
        point.y = Y1;
        LatLng firstGeoPoint = mGoogleMap.getProjection().fromScreenLocation(point);
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                if (mDrawFinished) {
                    X1 = (int) event.getX();
                    Y1 = (int) event.getY();
                    point = new Point();
                    point.x = X1;
                    point.y = Y1;
                    LatLng geoPoint = mGoogleMap.getProjection()
                            .fromScreenLocation(point);
                    mLatlngs.add(geoPoint);
                    mPolylineOptions = new PolylineOptions();
                    mPolylineOptions.color(Color.RED);
                    mPolylineOptions.width(3);
                    mPolylineOptions.addAll(mLatlngs);
                    mGoogleMap.addPolyline(mPolylineOptions);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "Poinnts array size " + mLatlngs.size());
                mLatlngs.add(firstGeoPoint);
                mGoogleMap.clear();
                mPolylineOptions = null;
                mMapShelterView.setVisibility(View.GONE);
                mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
                mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
                mPolygonOptions = new PolygonOptions();
                mPolygonOptions.fillColor(Color.GRAY);
                mPolygonOptions.strokeColor(Color.RED);
                mPolygonOptions.strokeWidth(5);
                mPolygonOptions.addAll(mLatlngs);
                mGoogleMap.addPolygon(mPolygonOptions);
                mDrawFinished = false;

                try {
                    printAddresses();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return mGestureDetector.onTouchEvent(event);
    }

    /**
     * Setting up map
     */

    private void initilizeMap() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {
            if (mGoogleMap == null) {
                mGoogleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                // mGoogleMap.setMyLocationEnabled(true);

            }

        } else if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
            // showErrorDialog(status);
        } else {
            Toast.makeText(this, "No Support for Google Play Service", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method gets called on tap of draw button, It prepares the screen to draw
     * the polygon
     *
     * @param view
     */

    public void drawZone(View view) {
        mGoogleMap.clear();
        mLatlngs.clear();
        mPolylineOptions = null;
        mPolygonOptions = null;
        mDrawFinished = true;
        mMapShelterView.setVisibility(View.VISIBLE);
        mGoogleMap.getUiSettings().setScrollGesturesEnabled(false);
    }

    private void printAddresses() throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        for(LatLng point : mLatlngs){
          List<Address> addressList = geocoder.getFromLocation(point.latitude, point.longitude, 1);
            if(addressList!=null && addressList.size() > 0){
                String address = addressList.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addressList.get(0).getLocality();
                String state = addressList.get(0).getAdminArea();
                String country = addressList.get(0).getCountryName();
                String postalCode = addressList.get(0).getPostalCode();
                String knownName = addressList.get(0).getFeatureName(); //
//                System.out.println("address " + address );
                System.out.println("city " + city );
//                System.out.println("state " + state );
//                System.out.println("country " + country );
                System.out.println("postalCode " + postalCode );
                System.out.println("knownName " + knownName );
            }
        }
    }

    public synchronized boolean Contains(Location location) {
        boolean isInside = false;
        if (mLatlngs.size() > 0) {
            LatLng lastPoint = mLatlngs.get(mLatlngs.size() - 1);

            double x = location.getLongitude();

            for (LatLng point : mLatlngs) {
                double x1 = lastPoint.longitude;
                double x2 = point.longitude;
                double dx = x2 - x1;

                if (Math.abs(dx) > 180.0) {
                    if (x > 0) {
                        while (x1 < 0)
                            x1 += 360;
                        while (x2 < 0)
                            x2 += 360;
                    } else {
                        while (x1 > 0)
                            x1 -= 360;
                        while (x2 > 0)
                            x2 -= 360;
                    }
                    dx = x2 - x1;
                }

                if ((x1 <= x && x2 > x) || (x1 >= x && x2 < x)) {
                    double grad = (point.latitude - lastPoint.latitude) / dx;
                    double intersectAtLat = lastPoint.latitude
                            + ((x - x1) * grad);

                    if (intersectAtLat > location.getLatitude())
                        isInside = !isInside;
                }
                lastPoint = point;
            }
        }

        return isInside;
    }
}
