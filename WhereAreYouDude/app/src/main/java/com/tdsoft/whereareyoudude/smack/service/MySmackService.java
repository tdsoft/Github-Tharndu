package com.tdsoft.whereareyoudude.smack.service;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.support.v7.app.NotificationCompat;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import com.tdsoft.whereareyoudude.R;
import com.tdsoft.whereareyoudude.smack.ConnectionManager;
import com.tdsoft.whereareyoudude.splash.SplashActivity;

public class MySmackService extends Service {
    private static final int NOTIFICATION_ID = 1000;
    private static final String STOP_FOREGROUND = "stop_foreground";
    private static final String EXIT_APP = "ExitApp";

    public MySmackService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ConnectionManager.getInstance().connect("MySmackService");
        startForeground();
        return START_NOT_STICKY;
    }

    private void startForeground() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EXIT_APP);
        registerReceiver(stopServiceReceiver, intentFilter);

        PendingIntent pendingIntentExitApp= PendingIntent.getBroadcast(this, 0, new Intent(EXIT_APP), PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Action exitAppAction = new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_close_clear_cancel, "ExitApp", pendingIntentExitApp).build();

        Bitmap bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_point),
                getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_width),
                getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_height),
                true);
        Intent intent = new Intent(this, SplashActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 01, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentTitle(getString(R.string.app_name));
        builder.setContentText("Click on icon to stop the service");
        builder.addAction(exitAppAction);
//        builder.setSubText("Some sub text");
        builder.setContentIntent(pendingIntent);
//        builder.setTicker("Fancy Notification");
        builder.setSmallIcon(android.R.color.transparent);
        builder.setLargeIcon(bm);
        builder.setAutoCancel(true);
        builder.setOngoing(true);
        builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        Notification notification = builder.build();
//        NotificationManager notificationManger =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManger.notify(01, notification);

        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(stopServiceReceiver);
        System.exit(0);
    }

    //We need to declare the receiver with onReceive function as below
    protected BroadcastReceiver stopServiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null && intent.getAction()!=null){
                if(intent.getAction().equals(EXIT_APP)){
                    ConnectionManager.getInstance().disconnect();
                    stopSelf();
                }
            }
        }
    };
}
