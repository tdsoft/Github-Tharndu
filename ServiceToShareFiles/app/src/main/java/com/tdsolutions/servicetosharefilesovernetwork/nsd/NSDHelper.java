package com.tdsolutions.servicetosharefilesovernetwork.nsd;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * Created by Admin on 10/25/2016.
 */

public class NSDHelper {
    private static final String TAG = "NSDHelper";
    public static final String SERVICE_TYPE = "_http._tcp.";
    public static final String SERVICE_NAME = "NsdChat";

    private final Context context;

    private ServerSocket mServerSocket;
    private int mLocalPort;

    private NsdManager mNsdManager;
    private String mServiceName;
    private NsdServiceInfo mService;

    private MyRegistrationListener mRegistrationListener;
    private MyDiscoveryListener mMyDiscoveryListener;


    public NSDHelper(Context context){
        this.context = context;
        mNsdManager = (NsdManager)context.getSystemService(Context.NSD_SERVICE);
    }

    public void startService(){
        // Initialize a server socket on the next available port.
        try {
            mServerSocket = new ServerSocket(0);
            mLocalPort =  mServerSocket.getLocalPort();
            registerService(mLocalPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerService(int port) {
        NsdServiceInfo serviceInfo  = new NsdServiceInfo();
        serviceInfo.setServiceName(SERVICE_NAME);
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(port);
        mRegistrationListener = new MyRegistrationListener();
        mNsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, mRegistrationListener);
    }

    public void discoverServices() {

        mMyDiscoveryListener = new MyDiscoveryListener();
        mNsdManager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD, mMyDiscoveryListener);
    }

    public void stopDiscovery(){
        mNsdManager.stopServiceDiscovery(mMyDiscoveryListener);
    }

    public void tearDown() {
        mNsdManager.unregisterService(mRegistrationListener);
        stopDiscovery();
    }




    public class MyDiscoveryListener implements NsdManager.DiscoveryListener{
        //  Called as soon as service discovery begins.
        @Override
        public void onDiscoveryStarted(String regType) {
            Log.d(TAG, "Service discovery started");
        }

        @Override
        public void onServiceFound(NsdServiceInfo service) {
            // A service was found!  Do something with it.
            Log.d(TAG, "Service discovery success" + service);
            if (!service.getServiceType().equals(SERVICE_TYPE)) {
                // Service type is the string containing the protocol and
                // transport layer for this service.
                Log.d(TAG, "Unknown Service Type: " + service.getServiceType());
            } else if (service.getServiceName().equals(mServiceName)) {
                // The name of the service tells the user what they'd be
                // connecting to. It could be "Bob's Chat App".
                Log.d(TAG, "Same machine: " + mServiceName);
            } else if (service.getServiceName().contains("NsdChat")){
                mNsdManager.resolveService(service, new MyResolveListener());
            }
        }

        @Override
        public void onServiceLost(NsdServiceInfo service) {
            // When the network service is no longer available.
            // Internal bookkeeping code goes here.
            Log.e(TAG, "service lost" + service);
        }

        @Override
        public void onDiscoveryStopped(String serviceType) {
            Log.i(TAG, "Discovery stopped: " + serviceType);
        }

        @Override
        public void onStartDiscoveryFailed(String serviceType, int errorCode) {
            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            mNsdManager.stopServiceDiscovery(this);
        }

        @Override
        public void onStopDiscoveryFailed(String serviceType, int errorCode) {
            Log.e(TAG, "Discovery failed: Error code:" + errorCode);
            mNsdManager.stopServiceDiscovery(this);
        }


    }




    public class MyRegistrationListener implements NsdManager.RegistrationListener {
        @Override
        public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {
            // Save the service name.  Android may have changed it in order to
            // resolve a conflict, so update the name you initially requested
            // with the name Android actually used.
            mServiceName = NsdServiceInfo.getServiceName();
        }



        @Override
        public void onRegistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            // Registration failed!  Put debugging code here to determine why.
            Log.d(TAG, "onRegistrationFailed " + serviceInfo.getServiceName() + " error code: " + errorCode);
        }

        @Override
        public void onServiceUnregistered(NsdServiceInfo arg0) {
            // Service has been unregistered.  This only happens when you call
            // NsdManager.unregisterService() and pass in this listener.
            Log.d(TAG, "onServiceUnregistered " + arg0.getServiceName());
        }

        @Override
        public void onUnregistrationFailed(NsdServiceInfo serviceInfo, int errorCode) {
            // Unregistration failed.  Put debugging code here to determine why.
            Log.d(TAG, "onUnregistrationFailed " + serviceInfo.getServiceName() + " error code: " + errorCode);
        }
    }

    public class MyResolveListener implements NsdManager.ResolveListener {
        private static final String TAG = "MyResolveListener";

        @Override
        public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
            // Called when the resolve fails.  Use the error code to debug.
            Log.e(TAG, "Resolve failed" + errorCode);
        }

        @Override
        public void onServiceResolved(NsdServiceInfo serviceInfo) {
            Log.e(TAG, "Resolve Succeeded. " + serviceInfo);

            if (serviceInfo.getServiceName().equals(mServiceName)) {
                Log.d(TAG, "Same IP.");
                return;
            }
            mService = serviceInfo;
            int port = mService.getPort();
            InetAddress host = mService.getHost();
        }
    }
}
