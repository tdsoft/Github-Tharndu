package com.tdsoft.whereareyoudude.splash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.tdsoft.whereareyoudude.LoginActivity;
import com.tdsoft.whereareyoudude.MainActivity;
import com.tdsoft.whereareyoudude.R;
import com.tdsoft.whereareyoudude.base.BaseActivity;
import com.tdsoft.whereareyoudude.smack.ConnectionManager;
import com.tdsoft.whereareyoudude.smack.service.MySmackService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jivesoftware.smack.XMPPConnection;

public class SplashActivity extends BaseActivity {

    private static final String TAG = "SplashActivity";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, MySmackService.class);
        startService(intent);

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnConnected(XMPPConnection xmppConnection) {
        progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
