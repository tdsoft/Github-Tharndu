package com.android.tdsoft.fileobservertest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.File;

/**
 * Created by Admin on 4/26/2016.
 */
public class MyForegroundService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        //your code goes here
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        keepServiceAlive();
        //your code goes here
        return(START_NOT_STICKY);
    }

    private void keepServiceAlive() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this).setContentTitle(getString(R.string.app_name))
                .setContentText("Hello")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(Notification.FLAG_ONGOING_EVENT, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(getClass().getName(), "Got to stop()!");
        stopForeground(true);
    }
}
