package com.android.tdsoft.androidappfoundation.base.data;

import com.android.tdsoft.androidappfoundation.base.AppBaseClass;
import com.android.tdsoft.androidappfoundation.base.utils.NetworkUtil;

/**
 * Created by Admin on 12/21/2015.
 */
public class ConnectionDetails extends AppBaseClass {
    private ConnectionType connectionType = ConnectionType.TYPE_NOT_CONNECTED;

    public boolean isConnected() {
        boolean status = false ;
        if (connectionType == ConnectionType.TYPE_WIFI) {
            status = true;
        } else if (connectionType == ConnectionType.TYPE_MOBILE) {
            status = true;
        } else if (connectionType == ConnectionType.TYPE_NOT_CONNECTED) {
            status = false;
        }
        return status;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public enum ConnectionType{
        TYPE_WIFI,
        TYPE_MOBILE,
        TYPE_NOT_CONNECTED
    }
}
