package com.eight25media.p2pfiletransfercrossplat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.eight25media.p2pfiletransfercrossplat.MainActivity.PORT;

public class AnotherWay extends AppCompatActivity {

    private static final String TAG = "AnotherWay";
    private static final int MY_PERMISSIONS_REQUEST_READ_WRITE = 100;
    private static final int TIMEOUT = 60000;
    private boolean isOkToSendNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_way);


        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_WRITE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    int i = 1;

    LinkedList<String> packetOrderMap = new LinkedList();
    Map<String, byte[]> receivedDate = new TreeMap<>();
    private int packetCount;

    public void onStartReceivingClick(View view) {
        Thread thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                try {
                    //Keep a socket open to listen to all the UDP trafic that is destined for this port
                    InetAddress addr = null;

                    for (InterfaceAddress interfaceAddress : getWlanEth().getInterfaceAddresses()) {
                        addr = interfaceAddress.getBroadcast();

                    }
//                    DatagramSocket socket = new DatagramSocket(PORT, InetAddress.getByName("0.0.0.0")); // workign
                    DatagramSocket socket = new DatagramSocket(PORT, addr);
                    socket.setBroadcast(true);


                    while (true) {

                        //Receive a packet
                        byte[] recvBuf = new byte[24000];
                        DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                        socket.receive(packet);
//                        String myIp = Utils.getIPAddress(true);
//                        Log.d(TAG, "MyIp " + myIp + " sender Ip: " + (packet.getSocketAddress().toString()));
//                        if((packet.getAddress()).getHostAddress().equals(myIp)){
//                            Log.i(TAG, "Packet from My Ip");
//                            return;
//                        }

                       // Log.i(TAG, "Ready to receive broadcast packets!");

                        //Packet received
//                        Log.i(TAG, "Packet received from: " + packet.getAddress().getHostAddress());
                        final String data = new String(packet.getData()).trim();

                        if (!canCreateJson(data)) {
                            return;
                        }
                        JSONObject jsonObject = new JSONObject(data);
                        BasePacket basePacket;
                        if (jsonObject.isNull("packetType") || !jsonObject.has("packetType")) {
                            return;
                        }

                        if (jsonObject.getInt("packetType") == BasePacket.INITIAL_PACKET) {
                            basePacket = new Gson().fromJson(data, InitialPacket.class);
                            InitialPacket initialPacket = (InitialPacket) basePacket;
                            Log.d(TAG, "File Name: " + initialPacket.fileName + "\nPacketOrder: " + initialPacket.packetOrder + "\nFile size: " + initialPacket.fileSize);
                            String[] packetIds = initialPacket.packetOrder.split(",");

                            for (String packetId : packetIds) {
                                if (packetId != null && packetId.length() > 0) {
                                    packetOrderMap.add(packetId);
                                }
                            }
                        } else if (jsonObject.getInt("packetType") == BasePacket.FILE_PACKET) {
                            basePacket = new Gson().fromJson(data, MyPacket.class);
                            MyPacket myPacket = (MyPacket) basePacket;
                            Log.d(TAG, "Received: " + myPacket.packetId);
                            receivedDate.put(myPacket.packetId.replace(',', ' ').trim(), myPacket.packetData);

                            ReceivedPacket receivedPacket = new ReceivedPacket();
                            receivedPacket.packetID = myPacket.packetId.replace(',', ' ').trim();
                            String receivedPackStr = new Gson().toJson(receivedPacket);
                            byte[] receivedPackStrData = receivedPackStr.getBytes();
                            sendBroadcast(receivedPackStrData);
                        } else if (jsonObject.getInt("packetType") == BasePacket.RECEIVED_PACKET) {
                            basePacket = new Gson().fromJson(data, ReceivedPacket.class);
                            ReceivedPacket myPacket = (ReceivedPacket) basePacket;
                            Log.d(TAG, "Received ack for: " + myPacket.packetID);
                            isOkToSendNext = true;
                        } else if (jsonObject.getInt("packetType") == BasePacket.FINISHED_PACKET) {
                            i = 1;
//                            if (packetOrderMap.size() == receivedDate.size()) {
                            File down = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                            File file = new File(down, "test1.jpg");

                                FileOutputStream fop = null;
                                    try {
                                        fop = new FileOutputStream(file, true);
                                        for (String packetID : packetOrderMap) {
                                            byte[] packet1 = receivedDate.get(packetID);
                                            if(packet1!=null)
                                                fop.write(packet1);

                                            System.out.println("Done " + i);
                                            i++;
                                        }
                                        fop.flush();
                                        fop.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();

                                    } finally {
                                        try {
                                            if (fop != null) {
                                                fop.close();
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();

                                        }
//                                }

                            }
                        }


                    }
                } catch (IOException ex) {
                    Log.i(TAG, "Oops" + ex.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    private boolean canCreateJson(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }


    private long sendTime;
    private long recvideTime;
    private MyPacket lastSendPacket;
    private boolean hasFinished;

    public void onSendMessageClick(View view) {
        filePackets.clear();



        Thread thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                InitialPacket initialPacket = getInitialPacketDetails();
                String length = new Gson().toJson(initialPacket);
                byte[] sendData = length.getBytes();
                sendBroadcast(sendData);

                if (filePackets != null) {

                    for (MyPacket packet : filePackets) {

                        String filePackStr = new Gson().toJson(packet);
                        byte[] filePackStrData = filePackStr.getBytes();
                        sendBroadcast(filePackStrData);

                        sendTime = System.currentTimeMillis();
                        isOkToSendNext = false;
                        hasFinished = false;
                        lastSendPacket = packet;

                        while (!isOkToSendNext && !hasFinished) {
                            recvideTime = System.currentTimeMillis();
                            Log.d(TAG, "Waiting for ack: " + packet.packetId + " duration " + (recvideTime - sendTime) );
                            if ((recvideTime - sendTime) >= TIMEOUT) {
                                String lastPacket = new Gson().toJson(lastSendPacket);
                                byte[] lastPacketStrData = lastPacket.getBytes();
                                sendBroadcast(lastPacketStrData);
                                Log.d(TAG, "Resent: " + lastSendPacket.packetId);
                                sendTime = System.currentTimeMillis();
                                isOkToSendNext = false;
                                lastSendPacket = packet;
                            }
                        }

                        Log.d(TAG, "Sent: " + packet.packetId + " duration " + (recvideTime - sendTime) );
                    }


                    FinishedPacket finishedPacket = new FinishedPacket();
                    String finishedPackStr = new Gson().toJson(finishedPacket);
                    byte[] finishedPackStrData = finishedPackStr.getBytes();
                    sendBroadcast(finishedPackStrData);
                    hasFinished = true;
                    Log.d(TAG, "Finished Sending");
                }
            }
        });
        thread.start();
    }

    private InetAddress currentBroadCastAddress;

    public void sendBroadcast(byte[] sendData) {
        // Hack Prevent crash (sending should be done using an async task)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
//            ArrayList<InetAddress> inetAddressEnumeration = Collections.list(getWlanEth().getInetAddresses());
            for (InterfaceAddress interfaceAddress : getWlanEth().getInterfaceAddresses()) {
                currentBroadCastAddress = interfaceAddress.getBroadcast();
            }
            InetAddress addr = currentBroadCastAddress;


            //Open a random port to send the package
            DatagramSocket socket = new DatagramSocket();
            socket.setBroadcast(true);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, addr, PORT);
            socket.send(sendPacket);

        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        }
    }

    public InitialPacket getInitialPacketDetails() {

        InitialPacket initialPacket = null;
        File down = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(down, "Test.jpg");
        if (file == null || !file.exists()) {
            return null;
        }
        getPackets(file);
        initialPacket = new InitialPacket();
        initialPacket.fileName = file.getName();
        initialPacket.fileSize = (int) file.length();
        initialPacket.packetOrder = packetOrder;
        return initialPacket;
    }

    private String packetOrder = "";

    List<MyPacket> filePackets = new ArrayList<>();

    private List<MyPacket> getPackets(File file) {
        int i = 1;
        filePackets.clear();
        try {
            splitFile(file);
//            InputStream is = new FileInputStream(file);
//            int length = (int) file.length();
//            int take = 4096;//size of your chunk
//            System.out.println("Packet count: " + (length / take));
//            byte[] bytes = new byte[take];
//            int offset = 0;
//            int a = 0;
//            do {
//                a = is.read(bytes, 0, take);
//                offset += a;
//                MyPacket packet = new MyPacket();
//                packet.packetId = "bb" + i + ",";
//                packet.packetData = bytes;
//                packetOrder += packet.packetId;
//                filePackets.add(packet);
//                i++;
//            } while (offset < length);
//            is.close();
//            is = null;

        } catch (Exception e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        }

        return filePackets;
    }

    private static int PART_SIZE = 4096;
    private static String FILE_NAME = "temp.jpg";

    private void splitFile(File inputFile) {
        FileInputStream inputStream;
        String newFileName;
        FileOutputStream filePart;
        int fileSize = (int) inputFile.length();
        int nChunks = 0, read = 0, readLength = PART_SIZE;
        byte[] byteChunkPart;
        try {
            inputStream = new FileInputStream(inputFile);
            while (fileSize > 0) {
                if (fileSize <= 5) {
                    readLength = fileSize;
                }
                byteChunkPart = new byte[readLength];
                read = inputStream.read(byteChunkPart, 0, readLength);
                fileSize -= read;
                assert (read == byteChunkPart.length);
                nChunks++;

                MyPacket packet = new MyPacket();
                packet.packetId = "bb" + i + ",";
                packet.packetData = byteChunkPart;
                packetOrder += packet.packetId;
                filePackets.add(packet);
                i++;
                byteChunkPart = null;
                filePart = null;
            }
            inputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

//    private void mergerFile() {
//        File ofile = new File(FILE_NAME);
//        FileOutputStream fos;
//        FileInputStream fis;
//        byte[] fileBytes;
//        int bytesRead = 0;
//        List<File> list = new ArrayList<File>();
//        list.add(new File(FILE_NAME + ".part0"));
//        list.add(new File(FILE_NAME + ".part1"));
//        list.add(new File(FILE_NAME + ".part2"));
//        list.add(new File(FILE_NAME + ".part3"));
//        list.add(new File(FILE_NAME + ".part4"));
//        list.add(new File(FILE_NAME + ".part5"));
//        list.add(new File(FILE_NAME + ".part6"));
//        list.add(new File(FILE_NAME + ".part7"));
//        try {
//            fos = new FileOutputStream(ofile, true);
//            for (File file : list) {
//                fis = new FileInputStream(file);
//                fileBytes = new byte[(int) file.length()];
//                bytesRead = fis.read(fileBytes, 0, (int) file.length());
//                assert (bytesRead == fileBytes.length);
//                assert (bytesRead == (int) file.length());
//                fos.write(fileBytes);
//                fos.flush();
//                fileBytes = null;
//                fis.close();
//                fis = null;
//            }
//            fos.close();
//            fos = null;
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//    }


    public byte[] getBytes(File file) {

        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bytes;
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

    public static String getBroadcast() throws SocketException {
        System.setProperty("java.net.preferIPv4Stack", "true");
        for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum.hasMoreElements(); ) {
            NetworkInterface ni = niEnum.nextElement();
            if (!ni.isLoopback()) {
                for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
                    return interfaceAddress.getBroadcast().toString().substring(1);
                }
            }
        }
        return null;
    }
}
