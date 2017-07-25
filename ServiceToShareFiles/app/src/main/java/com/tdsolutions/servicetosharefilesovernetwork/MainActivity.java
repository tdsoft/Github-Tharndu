package com.tdsolutions.servicetosharefilesovernetwork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.tdsolutions.servicetosharefilesovernetwork.nsd.NSDHelper;

public class MainActivity extends AppCompatActivity {
    NSDHelper nsdHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nsdHelper = new NSDHelper(this);
    }

    public void onServiceDiscovers(View view) {
        nsdHelper.discoverServices();
    }

    public void onServiceStart(View view) {
        nsdHelper.startService();
    }
}
