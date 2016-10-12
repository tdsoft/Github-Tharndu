package com.tdsoft.whereareyoudude;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.tdsoft.whereareyoudude.smack.ConnectionManager;
import com.tdsoft.whereareyoudude.smack.data.Authenticated;
import com.tdsoft.whereareyoudude.smack.data.User;
import com.tdsoft.whereareyoudude.smack.service.MySmackService;
import com.tdsoft.whereareyoudude.utils.DialogHelper;
import com.tdsoft.whereareyoudude.utils.PreferenceHandler;

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
        if(TextUtils.isEmpty(etUserName.getText())){
            EventBus.getDefault().post(new Authenticated(false));
            return;
        }

        if(TextUtils.isEmpty(etPassword.getText())){
            EventBus.getDefault().post(new Authenticated(false));
            return;
        }
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
        if(authenticated.isSuccess()) {
            User user = new User();
            user.setUserName(etUserName.getText().toString());
            user.setPassword(etPassword.getText().toString());
            PreferenceHandler.getInstance().saveUserNameAndPass(user);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            DialogHelper.showDialog("Failed","Please try again",this);
        }
    }



    @Override
    protected void onNewIntent(Intent newIntent) {
        super.onNewIntent(newIntent);
        handleIntent(newIntent);

    }

    private void handleIntent(Intent newIntent) {
        if (newIntent == null)
            return;
        if(ConnectionManager.getInstance().isHasLogout()){
            return;
        }
        if (ConnectionManager.getInstance().hasAuthenticated()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
