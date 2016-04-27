package com.android.tdsoft.fileobservertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CommonReceiver extends BroadcastReceiver {
    public CommonReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, DirectoryWatcher.class));
    }
}
