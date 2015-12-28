package com.android.tdsoft.androidappfoundation.base.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.tdsoft.androidappfoundation.base.data.ConnectionDetails;
import com.android.tdsoft.androidappfoundation.base.utils.NetworkUtil;

import de.greenrobot.event.EventBus;

public class NetworkStatusReceiver extends BroadcastReceiver {

    public NetworkStatusReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectionDetails connectionDetails = NetworkUtil.getConnectivity(context);
        EventBus.getDefault().post(connectionDetails);
    }
}
