package com.eight25media.insecurefilesender;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class DeviceListActivity extends AppCompatActivity implements AdapterDevices.OnItemClickListener {

    private static final String INSECURE_BLUETOOTH = "InsecureBluetooth";
    private static final String TAG = "DeviceListActivity";
    private static final int LOCATION_PERMS = 1000;
    private AdapterDevices mPairedDevicesArrayAdapter;
    private List<MyDevice> deviceList = new ArrayList();
    private BluetoothAdapter mBtAdapter;
    private static final int REQUEST_ENABLE_BT = 100;
    private boolean mState = true;
    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);

        getPermission();

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();


        if (mBtAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else {
            listenForDevices();
        }

        // Set result CANCELED in case the user backs out
        setResult(Activity.RESULT_CANCELED);

        // Initialize the button to perform device discovery
        Button scanButton = (Button) findViewById(R.id.button_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                doDiscovery();
//                v.setVisibility(View.GONE);
            }
        });

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new AdapterDevices(deviceList);
        mPairedDevicesArrayAdapter.setOnItemClickListener(this);

        // Find and set up the ListView for paired devices
        RecyclerView pairedListView = (RecyclerView) findViewById(R.id.devices);
        pairedListView.setLayoutManager(new LinearLayoutManager(this));
        pairedListView.setAdapter(mPairedDevicesArrayAdapter);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);
        // Get the local Bluetooth adapter

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                MyDevice myDevice = new MyDevice();
                myDevice.deviceAddress = device.getAddress();
                myDevice.deviceName = device.getName();
                myDevice.isPaired = true;
                deviceList.add(myDevice);
            }

            mPairedDevicesArrayAdapter.notifyDataSetChanged();
        }

    }

    private void getPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        LOCATION_PERMS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    private void listenForDevices() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BluetoothServerSocket server = mBtAdapter.listenUsingInsecureRfcommWithServiceRecord(INSECURE_BLUETOOTH, MY_UUID_INSECURE);
                    BluetoothSocket socket;
                    while (mState) {
                        socket = server.accept();
                        if (socket != null) {
                            synchronized (DeviceListActivity.this) {
//                        connected(socket, socket.getRemoteDevice());
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null) {
            mBtAdapter.cancelDiscovery();
        }
        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    /**
     * Start device discover with the BluetoothAdapter
     */
    private void doDiscovery() {
        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }
        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    MyDevice myDevice = new MyDevice();
                    myDevice.deviceAddress = device.getAddress();
                    myDevice.deviceName = device.getName();
                    myDevice.isPaired = false;
                    deviceList.add(myDevice);
                    mPairedDevicesArrayAdapter.notifyDataSetChanged();
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setTitle(R.string.select_device);
            }
        }
    };

    @Override
    public void onItemClicked(int position) {
        new ConnectThread(mBtAdapter.getRemoteDevice(deviceList.get(position).deviceAddress), false).start();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    listenForDevices();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }


    public class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;
        private String mSocketType;

        public ConnectThread(BluetoothDevice device, boolean secure) {
            mmDevice = device;
            BluetoothSocket tmp = null;
            mSocketType = secure ? "Secure" : "Insecure";
            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {

                tmp = device.createInsecureRfcommSocketToServiceRecord(
                        MY_UUID_INSECURE);

            } catch (IOException e) {
                Log.e(TAG, "Socket Type: " + mSocketType + "create() failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectThread SocketType:" + mSocketType);
            setName("ConnectThread" + mSocketType);
            // Always cancel discovery because it will slow down a connection
            mBtAdapter.cancelDiscovery();
            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() " + mSocketType +
                            " socket during connection failure", e2);
                }
                return;
            }
            // Reset the ConnectThread because we're done
//            synchronized (BluetoothChatService.this) {
//                mConnectThread = null;
//            }
            // Start the connected thread
            connected(mmSocket, mmDevice, mSocketType);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect " + mSocketType + " socket failed", e);
            }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice, String mSocketType) {
        Log.d(TAG, "connected to " +mmDevice.getName());
    }
}
