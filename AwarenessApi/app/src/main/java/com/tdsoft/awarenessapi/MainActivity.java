package com.tdsoft.awarenessapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.tdsoft.awarenessapi.googlehelper.GoogleClientController;
import com.tdsoft.awarenessapi.googlehelper.NamedFence;
import com.tdsoft.awarenessapi.utils.AppUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int PLACE_PICKER_REQUEST = 100;
    List<NamedFence> namedFences = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void displayPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Log.d("PlacesAPI Demo", "GooglePlayServicesRepairableException thrown");
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.d("PlacesAPI Demo", "GooglePlayServicesNotAvailableException thrown");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            displayPlace(PlacePicker.getPlace(this, data));
        }
    }

    private void displayPlace(Place place) {
        if (place == null) {
            return;
        }
        NamedFence namedFence = new NamedFence();
        if (place.getAddress() != null && place.getAddress().length() > 0) {
            namedFence.setAddress(place.getAddress().toString());
        } else {
            try {
                namedFence.setAddress(AppUtil.getAddress(this, place.getLatLng().latitude, place.getLatLng().longitude));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        namedFence.setLatitude(place.getLatLng().latitude);
        namedFence.setLongitude(place.getLatLng().longitude);
        namedFences.add(namedFence);
        GoogleClientController.getInstance().startMonitoringGeoFences(namedFences);
        GoogleClientController.getInstance().startMonitoringHeadphonePlug();
        GoogleClientController.getInstance().startMonitoringActivity(DetectedActivityFence.IN_VEHICLE,DetectedActivityFence.STILL, DetectedActivityFence.WALKING);
    }

    public void onAddPlaceClick(View view) {
        displayPlacePicker();
    }
}
