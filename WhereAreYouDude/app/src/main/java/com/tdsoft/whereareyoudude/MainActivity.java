package com.tdsoft.whereareyoudude;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tdsoft.whereareyoudude.smack.ConnectionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void onLogOutClick(View view) {
        ConnectionManager.getInstance().logout();
    }
}
