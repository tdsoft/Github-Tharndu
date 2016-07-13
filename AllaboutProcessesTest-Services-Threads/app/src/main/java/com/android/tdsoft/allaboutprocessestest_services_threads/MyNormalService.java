package com.android.tdsoft.allaboutprocessestest_services_threads;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/*Description: I have started this service as in normal way. So obviously we know that this type of services will be killed
soon after the app is being killed.
 */
public class MyNormalService extends Service {
    public MyNormalService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand");
        return START_NOT_STICKY;

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.out.println("onLowMemory");
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        System.out.println("onTaskRemoved");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy");
    }
}
