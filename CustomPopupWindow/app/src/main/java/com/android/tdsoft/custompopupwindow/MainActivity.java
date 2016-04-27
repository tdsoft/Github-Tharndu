package com.android.tdsoft.custompopupwindow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

public class MainActivity extends AppCompatActivity {
    ViewGroup viewGroup;
    CustomPopup customPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewGroup = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.layout_popup, null, false);

    }

    public void OnClick(View view) {
        customPopup = new CustomPopup(this, view).show();
    }


    public void onOkClick(View view) {
        customPopup = new CustomPopup(this, view).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        customPopup.dismiss();
    }
}
