package com.tdsoft.whereareyoudude;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.tdsoft.whereareyoudude.smack.ConnectionManager;
import com.tdsoft.whereareyoudude.smack.data.Authenticated;
import com.tdsoft.whereareyoudude.smack.service.MySmackService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends AppCompatActivity {
    private EditText etUserName, etPassword;
    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.msg_pls_wait));

        etUserName = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        handleIntent(getIntent());
    }

    public void onClick(View view) {
        mProgressDialog.show();
        ConnectionManager.getInstance().login(etUserName.getText().toString(), etPassword.getText().toString());
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        Intent intent = new Intent(this, MySmackService.class);
        startService(intent);

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
        mProgressDialog.dismiss();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAuthenticated(Authenticated authenticated) {
        mProgressDialog.dismiss();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onNewIntent(Intent newIntent) {
        super.onNewIntent(newIntent);
        handleIntent(newIntent);

    }

    private void handleIntent(Intent newIntent) {
        if (newIntent == null)
            return;
        if (ConnectionManager.getInstance().hasAuthenticated()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
