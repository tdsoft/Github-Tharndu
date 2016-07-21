package com.tdsoft.whereareyoudude.smack;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.tdsoft.whereareyoudude.smack.data.Authenticated;
import com.tdsoft.whereareyoudude.smack.data.ExceptionFailedConnection;

import org.greenrobot.eventbus.EventBus;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;

/**
 * Created by Admin on 7/14/2016.
 */
public class ConnectionManager implements ConnectionListener {
    private static final String TAG = "connectionManager";


    private static final String HOST = "192.168.8.120";
    private static final int PORT = 5222;
    private static final long TIME_OUT = 5000;
    private static ConnectionManager INSTANCE;
    private static boolean initSuccess;
    private Context mContext;
    private XMPPTCPConnectionConfiguration mXmpptcpConnectionConfiguration;
    private XMPPTCPConnection mXmpptcpConnection;

    public static boolean isconnecting = false;
    public static boolean isToasted = true;
    public static boolean connected = false;
    private boolean hasLogout;

    public static ConnectionManager getInstance() {

        if (INSTANCE == null) {
            INSTANCE = new ConnectionManager();
        }
        return INSTANCE;
    }

    public ConnectionManager init(Context context) throws XmppStringprepException {
        this.mContext = context;

        DomainBareJid serviceName = JidCreate.domainBareFrom("lenovo-pc");
        mXmpptcpConnectionConfiguration = XMPPTCPConnectionConfiguration
                .builder()
                .setHost(HOST)
                .setPort(PORT)
                .setDebuggerEnabled(true)
                .setServiceName(serviceName)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .build();
        XMPPTCPConnection.setUseStreamManagementResumptionDefault(true);
        XMPPTCPConnection.setUseStreamManagementDefault(true);
        mXmpptcpConnection = new XMPPTCPConnection(mXmpptcpConnectionConfiguration);
        mXmpptcpConnection.setUseStreamManagement(true);
        mXmpptcpConnection.setPacketReplyTimeout(TIME_OUT);
        mXmpptcpConnection.addConnectionListener(this);
        initSuccess = true;
        return this;
    }


    public void connect(final String caller) {

        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected synchronized Boolean doInBackground(Void... arg0) {
                if (mXmpptcpConnection.isConnected()) {
                    connected(mXmpptcpConnection);
                    return false;
                }
                isconnecting = true;
                if (isToasted)
                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(mContext,caller + "=>connecting....", Toast.LENGTH_LONG).show();
                        }
                    });
                Log.d(TAG, caller + "=>connecting....");

                try {
                    mXmpptcpConnection.connect();
                    connected = true;

                } catch (IOException e) {
                    if (isToasted)
                        new Handler(Looper.getMainLooper())
                                .post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext, "(" + caller + ")" + "IOException: ",  Toast.LENGTH_SHORT).show();
                                    }
                                });

                    Log.e(TAG, "IOException: " + e.getMessage());
                    sayConnectFailed(e);
                } catch (SmackException e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(mContext,"(" + caller + ")" + "SMACKException: ", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e(TAG, "SMACKException: " + e.getMessage());
                    sayConnectFailed(e);
                } catch (XMPPException e) {
                    if (isToasted)

                        new Handler(Looper.getMainLooper())
                                .post(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext,"(" + caller + ")" + "XMPPException: ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    Log.e(TAG, "XMPPException: " + e.getMessage());
                    sayConnectFailed(e);
                } catch (InterruptedException e) {
                    if (isToasted)

                        new Handler(Looper.getMainLooper())
                                .post(new Runnable() {

                                    @Override
                                    public void run() {
                                        Toast.makeText(mContext,"(" + caller + ")" + "InterruptedException: ", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    Log.e(TAG, "XMPPException: " + e.getMessage());
                    sayConnectFailed(e);
                }

                return isconnecting = false;
            }
        };
        connectionThread.execute();
    }

    public void disconnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mXmpptcpConnection.disconnect();
            }
        }).start();
    }

    public XMPPConnection getConnection(){
        return mXmpptcpConnection;
    }

    public boolean hasAuthenticated(){
        boolean status = false;
        if(mXmpptcpConnection!=null && mXmpptcpConnection.isConnected() && mXmpptcpConnection.isAuthenticated()){
            status = true;
        }
        return status;
    }

    public void login(final String userName, final String password){
        AsyncTask<Void,Void,Void> taskLogin = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if(mXmpptcpConnection!=null && mXmpptcpConnection.isConnected()){
                    try {

                        if(!mXmpptcpConnection.isAuthenticated()) {
                            mXmpptcpConnection.login(userName, password);
                            if(mXmpptcpConnection.isAuthenticated()) {
                                EventBus.getDefault().post(new Authenticated());
                            }
                        }else{
                            if(hasLogout){
                                mXmpptcpConnection.login(userName, password);
                                hasLogout = false;
                            }
                            if(mXmpptcpConnection.isAuthenticated()) {
                                EventBus.getDefault().post(new Authenticated());
                            }
                        }


                    }catch (SmackException.AlreadyLoggedInException e){
                        sendAvailablePresence();
                    }catch (XMPPException e) {
                        e.printStackTrace();
                    } catch (SmackException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };

        taskLogin.execute();

    }


    private void sayConnectSuccess(XMPPConnection xmppConnection) {
        EventBus.getDefault().post(xmppConnection);
    }

    private void sayConnectFailed(Exception e) {
        EventBus.getDefault().post(e);
    }

    @Override
    public void connected(XMPPConnection connection) {
        ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(mXmpptcpConnection);
        reconnectionManager.enableAutomaticReconnection();
        sayConnectSuccess(connection);

    }

    @Override
    public void authenticated(XMPPConnection connection, boolean resumed) {
        Log.d(TAG, "authenticated and resumed " + resumed);
    }

    @Override
    public void connectionClosed() {
        Log.d(TAG, "connectionClosed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        Log.d(TAG, "connectionClosedOnError :" + e.getMessage());
    }

    @Override
    public void reconnectionSuccessful() {
        Log.d(TAG, "reconnectionSuccessful");
    }

    @Override
    public void reconnectingIn(int seconds) {
        Log.d(TAG, "reconnectingIn :" + seconds);
    }

    @Override
    public void reconnectionFailed(Exception e) {
        Log.d(TAG, "reconnectionFailed :" + e.getMessage());
    }

    public void logout() {
        sendUnavailablePresence();
    }

    public void sendAvailablePresence(){
        if(hasAuthenticated()){
            Presence offlinePres = new Presence(Presence.Type.available, "", 1, Presence.Mode.available);
            try {
                mXmpptcpConnection.sendStanza(offlinePres);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendUnavailablePresence(){
        if(hasAuthenticated()){
            Presence offlinePres = new Presence(Presence.Type.unavailable, "", 1, Presence.Mode.away);
            try {
                mXmpptcpConnection.sendStanza(offlinePres);
                hasLogout = true;
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
