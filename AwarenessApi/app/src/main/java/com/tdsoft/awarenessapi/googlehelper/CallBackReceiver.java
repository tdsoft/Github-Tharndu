package com.tdsoft.awarenessapi.googlehelper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.location.Geofence;
import com.tdsoft.awarenessapi.MainActivity;
import com.tdsoft.awarenessapi.MyApp;
import com.tdsoft.awarenessapi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 7/12/2016.
 */
public class CallBackReceiver extends BroadcastReceiver {


    private static final String TAG = "CallBackReceiver";
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext = context;
        FenceState fenceState = FenceState.extract(intent);

        Log.d(TAG, "Fence Receiver Received");

        switch (fenceState.getCurrentState()) {
            case FenceState.TRUE:
                Log.i(TAG, "Fence > Headphones are plugged in.");
                sendNotification(fenceState.getFenceKey());
                break;
            case FenceState.FALSE:
                Log.i(TAG, "Fence > Headphones are NOT plugged in.");
                break;
            case FenceState.UNKNOWN:
                Log.i(TAG, "Fence > The headphone fence is in an unknown state.");
                break;
        }

    }

    private void sendNotification(String notificationDetails) {
        // Create an explicit content Intent that starts the main Activity.
        Intent notificationIntent = new Intent(mContext, MainActivity.class);

        // Construct a task stack.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        // Push the content Intent onto the stack.
        stackBuilder.addNextIntent(notificationIntent);

        // Get a PendingIntent containing the entire back stack.
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get a notification builder that's compatible with platform versions >= 4
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);

        // Define the notification settings.
        builder.setSmallIcon(R.mipmap.ic_launcher)
                // In a real app, you may want to use a library like Volley
                // to decode the Bitmap.
                .setLargeIcon(BitmapFactory.decodeResource(MyApp.getInstance().getResources(),
                        R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText(MyApp.getInstance().getString(R.string.geofence_transition_notification_text))
                .setContentIntent(notificationPendingIntent);

        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) MyApp.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);

        // Issue the notification
        mNotificationManager.notify(0, builder.build());
    }


    private String getGeofenceTransitionDetails(
            Context context,
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }

    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                return MyApp.getInstance().getString(R.string.geofence_transition_entered);
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                return MyApp.getInstance().getString(R.string.geofence_transition_exited);
            default:
                return MyApp.getInstance().getString(R.string.unknown_geofence_transition);
        }
    }

}
