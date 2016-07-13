package com.android.tdsoft.allaboutprocessestest_services_threads;

import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void onStartNormalClick(View view) {
        Intent intentNormalService =  new Intent(this,MyNormalService.class);
        startService(intentNormalService);
    }

    public void onServiceAsASeparateProcessClick(View view) {
        Intent intentNormalService =  new Intent(this,ServiceAsASeparateProcess.class);
        startService(intentNormalService);
    }

    public void onForegroundServiceClick(View view) {
        Intent intentNormalService =  new Intent(this,MyForegroundService.class);
        startService(intentNormalService);
    }

    public void onStartActivities(View view) {
        TaskStackBuilder intents = TaskStackBuilder.create(this);
        intents.addParentStack(ActivityA.class);
        intents.addNextIntent(new Intent(this,ActivityA.class));
        intents.addNextIntent(new Intent(this,ActivityB.class));
        intents.addNextIntent(new Intent(this,ActivityC.class));
        intents.startActivities();
    }
}
