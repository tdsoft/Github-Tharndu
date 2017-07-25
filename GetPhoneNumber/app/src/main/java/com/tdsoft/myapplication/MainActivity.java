package com.tdsoft.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String number = getMyPhoneNO();
        Toast.makeText(getApplicationContext(), "My Phone Number is: " + number, Toast.LENGTH_SHORT).show();

        TextView textView = (TextView) findViewById(R.id.phone_number_view);
        textView.setText("My Phone Nnumber is: " + number);
    }

    private String getMyPhoneNO() {
        TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return tMgr.getLine1Number();
    }
}
