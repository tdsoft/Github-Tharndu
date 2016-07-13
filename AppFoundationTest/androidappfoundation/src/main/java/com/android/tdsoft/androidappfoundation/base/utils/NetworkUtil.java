package com.android.tdsoft.androidappfoundation.base.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.tdsoft.androidappfoundation.base.data.ConnectionDetails;

/**
 * Created by Admin on 12/21/2015.
 */
public class NetworkUtil {

    public static ConnectionDetails getConnectivity(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        ConnectionDetails connectionDetails = new ConnectionDetails();
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                connectionDetails.setConnectionType(ConnectionDetails.ConnectionType.TYPE_WIFI);
            }
            else if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                connectionDetails.setConnectionType(ConnectionDetails.ConnectionType.TYPE_MOBILE);
            }
        }
        return connectionDetails;
    }


}
