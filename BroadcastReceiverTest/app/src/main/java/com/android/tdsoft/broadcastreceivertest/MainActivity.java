package com.android.tdsoft.broadcastreceivertest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String BROADCAST_ACTION = "com.aj.SHOWTOAST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendBroadcast(View v) {
        Intent broadcast = new Intent();
        broadcast.setAction(BROADCAST_ACTION);
        this.sendBroadcast(broadcast);
    }
}
