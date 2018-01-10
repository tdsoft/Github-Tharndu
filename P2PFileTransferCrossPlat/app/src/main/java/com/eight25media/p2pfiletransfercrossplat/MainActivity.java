package com.eight25media.p2pfiletransfercrossplat;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.eight25media.p2pfiletransfercrossplat.udpmulicastlib.UDPMessenger;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
    final static int PORT = 8888;
    final static String INET_ADDR = "224.0.0.3";
    private static final String TAG = "piku";
    private UDPMessenger udpMessenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        udpMessenger = new UDPMessenger(this, "Kuku", 15000) {
//            @Override
//            protected Runnable getIncomingMessageAnalyseRunnable() {
//                System.out.println(incomingMessage.getMessage());
//                return null;
//            }
//        };


        NetworkInterface networkInterface = getWlanEth();

    }

    public static NetworkInterface getWlanEth() {
        Enumeration<NetworkInterface> enumeration = null;
        try {
            enumeration = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        NetworkInterface wlan0 = null;
        StringBuilder sb = new StringBuilder();
        while (enumeration.hasMoreElements()) {
            wlan0 = enumeration.nextElement();
            sb.append(wlan0.getName() + " ");
            if (wlan0.getName().equals("wlan0")) {
                //there is probably a better way to find ethernet interface
                Log.i(TAG, "wlan0 found");
                return wlan0;
            }
        }

        return null;
    }

    public void onStartReceivingClick(View view) {
        Thread thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                // Create a buffer of bytes, which will be used to store
                // the incoming bytes containing the information from the server.
                // Since the message is small here, 256 bytes should be enough.
                byte[] buf = new byte[256];

                // Create a new Multicast socket (that will allow other sockets/programs
                // to join it as well.
                try (MulticastSocket clientSocket = new MulticastSocket(PORT)) {
                    //Joint the Multicast group.
                    clientSocket.joinGroup(new InetSocketAddress(INET_ADDR, PORT), getWlanEth());

                    while (true) {
                        // Receive the information and print it.
                        DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                        clientSocket.receive(msgPacket);

                        String msg = new String(buf, 0, buf.length);
                        System.out.println("Socket 1 received msg: " + msg);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void onSendMessageClick(View view) {
        Thread thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                // Get the address that we are going to connect to.
                ArrayList<InetAddress> inetAddressEnumeration = Collections.list(getWlanEth().getInetAddresses());

                InetAddress addr = inetAddressEnumeration.get(1);


                // Open a new DatagramSocket, which will be used to send the data.
                try (DatagramSocket serverSocket = new DatagramSocket()) {

                    for (int i = 0; i < 5; i++) {
                        String msg = "Sent message no " + i;

                        // Create a packet that will contain the data
                        // (in the form of bytes) and send it.
                        DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, addr, PORT);
                        System.out.println(msgPacket.getAddress().toString());
                        serverSocket.send(msgPacket);

                        System.out.println("Server sent packet with msg: " + msg);
                        Thread.sleep(500);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        //udpMessenger.stopMessageReceiver();
    }

    public void onHotspotStartClick(View view) {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if(wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(false);
        }

        WifiConfiguration netConfig = new WifiConfiguration();

        netConfig.SSID = "MyAP";
        netConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        netConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        netConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        try{
            Method setWifiApMethod = wifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            boolean apstatus=(Boolean) setWifiApMethod.invoke(wifiManager, netConfig,true);

            Method isWifiApEnabledmethod = wifiManager.getClass().getMethod("isWifiApEnabled");
            while(!(Boolean)isWifiApEnabledmethod.invoke(wifiManager)){};
            Method getWifiApStateMethod = wifiManager.getClass().getMethod("getWifiApState");
            int apstate=(Integer)getWifiApStateMethod.invoke(wifiManager);
            Method getWifiApConfigurationMethod = wifiManager.getClass().getMethod("getWifiApConfiguration");
            netConfig=(WifiConfiguration)getWifiApConfigurationMethod.invoke(wifiManager);
            Log.e("CLIENT", "\nSSID:"+netConfig.SSID+"\nPassword:"+netConfig.preSharedKey+"\n");

        } catch (Exception e) {
            Log.e(this.getClass().toString(), "", e);
        }
    }

    public void onAnotherWayClick(View view) {
        Intent intent = new Intent(this, AnotherWay.class);
        startActivity(intent);
    }
}
