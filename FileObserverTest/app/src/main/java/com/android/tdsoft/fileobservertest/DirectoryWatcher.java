package com.android.tdsoft.fileobservertest;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.io.File;

public class DirectoryWatcher extends Service {
    //Class variables
    MyDirObserver myDirObserver = null;
    String dirName = "Demo";
    String pathToWatchPic = "";

    public DirectoryWatcher() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        pathToWatchPic = Environment.getExternalStorageDirectory().toString() + "/" + dirName;

        File mediaStorageDir = new File(pathToWatchPic);
        if (!mediaStorageDir.exists()){
            pathToWatchPic = Environment.getExternalStorageDirectory().toString() + "/" + dirName;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (myDirObserver != null){
            myDirObserver.stopWatching();
        }
        //each time service is restarted, observPictures object is recreated
        //and observation is restarted. This way File Observer never stops.
        myDirObserver = new MyDirObserver(this,pathToWatchPic);
        myDirObserver.startWatching();

        keepServiceAlive();
        return(START_NOT_STICKY);
    }

    private void keepServiceAlive() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this).setContentTitle(getString(R.string.app_name))
                .setContentText("Directory Watching: " + dirName)
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
