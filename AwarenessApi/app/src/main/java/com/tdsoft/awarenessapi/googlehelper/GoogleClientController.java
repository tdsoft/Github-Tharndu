package com.tdsoft.awarenessapi.googlehelper;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.DetectedActivityFence;
import com.google.android.gms.awareness.fence.FenceUpdateRequest;
import com.google.android.gms.awareness.fence.HeadphoneFence;
import com.google.android.gms.awareness.fence.LocationFence;
import com.google.android.gms.awareness.state.HeadphoneState;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.tdsoft.awarenessapi.R;
import com.tdsoft.awarenessapi.utils.Toaster;

import java.util.List;

/**
 * Created by Admin on 7/12/2016.
 */
public class GoogleClientController implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "GoogleClientController";
    private static final double RADIUS = 100;
    private static GoogleClientController INSTANCE;
    private Context mContext;
    private PendingIntent mFencePendingIntent;
    private List<NamedFence> mNamedFences;
    private GoogleApiClient mGoogleApiClient;
    private boolean isSuccessGoogleClient;

    private static final String HEADPHONE_FENCE = "headphoneFence";
    private static final String ACTIVITY_FENCE = "activityFence";

    public static GoogleClientController getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GoogleClientController();
        }
        return INSTANCE;
    }

    public void init(Context context) {
        this.mContext = context;
        this.isSuccessGoogleClient = false;
        initGoogleClient();
    }

    public void startMonitoringGeoFences(List<NamedFence> namedFenceList) {
        this.mNamedFences = namedFenceList;
        Intent intent = new Intent("com.tdsoft.android.geofence.ACTION_RECEIVE_GEOFENCE");
        mFencePendingIntent = PendingIntent.getBroadcast(mContext, 10001, intent, 0);

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        for (NamedFence namedFence : namedFenceList) {
            AwarenessFence awarenessFence = LocationFence.in(namedFence.getLatitude(), namedFence.getLongitude(), RADIUS, 10000);
            registerFence(namedFence.getId(), awarenessFence);
        }
    }

    @IntDef({DetectedActivityFence.IN_VEHICLE, DetectedActivityFence.ON_BICYCLE, DetectedActivityFence.ON_FOOT, DetectedActivityFence.RUNNING, DetectedActivityFence.STILL, DetectedActivityFence.TILTING,
            DetectedActivityFence.WALKING, DetectedActivityFence.UNKNOWN})
    public @interface ActivityType{}

    public void startMonitoringActivity(@ActivityType int... activities) {
        AwarenessFence activityFence = DetectedActivityFence.during(activities);
        registerFence(ACTIVITY_FENCE, activityFence);
    }

    public void startMonitoringHeadphonePlug() {
        AwarenessFence headphoneFence = HeadphoneFence.during(HeadphoneState.PLUGGED_IN);
        registerFence(HEADPHONE_FENCE, headphoneFence);
    }

    public void stopMonitoringHeadphonePlugIn() {
        unregisterFence(HEADPHONE_FENCE);
    }

    protected void registerFence(final String fenceKey, final AwarenessFence fence) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Awareness.FenceApi.updateFences(
                    mGoogleApiClient,
                    new FenceUpdateRequest.Builder()
                            .addFence(fenceKey, fence, mFencePendingIntent)
                            .build())
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            if (status.isSuccess()) {
                                Log.i(TAG, "Fence was successfully registered.");
                            } else {
                                Log.e(TAG, "Fence could not be registered: " + status);
                            }
                        }
                    });
        } else {
            Toaster.showShortMsg(R.string.err_google_client_not_connected);
        }
    }

    protected void unregisterFence(final String fenceKey) {
        Awareness.FenceApi.updateFences(
                mGoogleApiClient,
                new FenceUpdateRequest.Builder()
                        .removeFence(fenceKey)
                        .build()).setResultCallback(new ResultCallbacks<Status>() {
            @Override
            public void onSuccess(@NonNull Status status) {
                Log.i(TAG, "Fence " + fenceKey + " successfully removed.");
            }

            @Override
            public void onFailure(@NonNull Status status) {
                Log.i(TAG, "Fence " + fenceKey + " could NOT be removed.");
            }
        });
    }

    private void initGoogleClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(Awareness.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        this.isSuccessGoogleClient = true;
        Toaster.showShortMsg(R.string.msg_connected_google_client);
    }

    @Override
    public void onConnectionSuspended(int i) {
        this.isSuccessGoogleClient = false;
        Toaster.showShortMsg(R.string.err_google_client_connection_suspend);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        this.isSuccessGoogleClient = false;
        Toaster.showShortMsg(R.string.err_connecting_google_client);
    }
}
