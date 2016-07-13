package com.android.tdsoft.allaboutprocessestest_services_threads;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


/*Description: Even if we start this service as a separate process we can't keep it alive forever
  and it will destroy soon after the app is being killed by the user
 */

public class ServiceAsASeparateProcess extends Service {

    public ServiceAsASeparateProcess() {

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
