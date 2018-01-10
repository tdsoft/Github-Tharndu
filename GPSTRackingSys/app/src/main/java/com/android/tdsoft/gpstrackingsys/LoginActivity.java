package com.android.tdsoft.gpstrackingsys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText txtPassword;
    private EditText txtPhone;
    private EditText txtZoom;
    private CheckBox chkContinuousMarker,chkGeoFence;
    private EditText txtFenceRadius;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtZoom = (EditText) findViewById(R.id.txtZoom);
        txtFenceRadius = (EditText) findViewById(R.id.txtFenceRadius);
        chkContinuousMarker = (CheckBox) findViewById(R.id.chkContinuousMarker);
        chkGeoFence = (CheckBox) findViewById(R.id.chkGeoFence);

        if (!TextUtils.isEmpty(MyPref.getInstance().getPhoneNumber())) {
            txtPhone.setText(MyPref.getInstance().getPhoneNumber());
        }
        txtFenceRadius.setText(String.valueOf(MyPref.getInstance().getGeoFenceRadius()));
        txtZoom.setText(String.valueOf(MyPref.getInstance().getZoom()));
        chkContinuousMarker.setChecked(MyPref.getInstance().isAddContinuousMarker());
        chkGeoFence.setChecked(MyPref.getInstance().isGeoFenceEnable());
    }

    public void onLoginClick(View view) {
        if (!TextUtils.isEmpty(txtPassword.getText()) && txtPassword.getText().toString().equals("1987")) {
            if (!TextUtils.isEmpty(txtPhone.getText()) && txtPhone.getText().length() >= 10) {
                MyPref.getInstance().setPhoneNumber(txtPhone.getText().toString());
            }

            if (!TextUtils.isEmpty(txtZoom.getText()) && txtZoom.getText().length() >= 0) {
                MyPref.getInstance().setZoom(Float.parseFloat(txtZoom.getText().toString()));
            }
            if (!TextUtils.isEmpty(txtFenceRadius.getText()) && txtFenceRadius.getText().length() >= 0) {
                MyPref.getInstance().setGeofenceRadius(Float.parseFloat(txtFenceRadius.getText().toString()));
            }

            MyPref.getInstance().setAddContinuousMarker(chkContinuousMarker.isChecked());
            MyPref.getInstance().setGeoFenceEnable(chkGeoFence.isChecked());

            finish();
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
    }
}
