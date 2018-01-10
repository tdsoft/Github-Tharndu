package com.android.tdsoft.gpstrackingsys;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import de.greenrobot.event.EventBus;

public class MySmsReceiver extends BroadcastReceiver {

    private final Gson gson;

    public MySmsReceiver() {
        gson = new Gson();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle myBundle = intent.getExtras();
        SmsMessage[] messages = null;
        String strMessage = "";

        if (myBundle != null) {
            Object[] pdus = (Object[]) myBundle.get("pdus");

            messages = new SmsMessage[pdus.length];

            for (int i = 0; i < messages.length; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String format = myBundle.getString("format");
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
//                strMessage += "SMS From: " + messages[i].getOriginatingAddress();
//                strMessage += " : ";
                strMessage += messages[i].getMessageBody();
//                strMessage += "\n";
            }

            try {
                AppMessage appMessage = gson.fromJson(strMessage, AppMessage.class);
                EventBus.getDefault().post(appMessage);
            }catch (Exception e) {
                e.printStackTrace();
            }

            //Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();
        }

        this.abortBroadcast();
    }


}
