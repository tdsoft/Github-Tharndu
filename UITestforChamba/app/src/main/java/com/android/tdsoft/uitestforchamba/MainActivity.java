package com.android.tdsoft.uitestforchamba;

import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Environment;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Chronometer chronometer;
    private Button btn_en;
    private Button btn_ru;
    private Button btn_de;
    private Button btn_fr;
    private TextView txt_hello;
    AnswerController answerController;
    List<MyListItem> myListItems;
    LinearLayout container;
    private AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            System.out.println(focusChange);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocaleHelper.onCreate(this);
        setContentView(R.layout.activity_main);
        chronometer = (Chronometer) findViewById(R.id.chronometer);
        ((Button) findViewById(R.id.start_button)).setOnClickListener(this);
        ((Button) findViewById(R.id.stop_button)).setOnClickListener(this);

        btn_en = ((Button) findViewById(R.id.btn_en));
        btn_en.setOnClickListener(this);
        btn_ru = ((Button) findViewById(R.id.btn_ru));
        btn_ru.setOnClickListener(this);
        btn_de = ((Button) findViewById(R.id.btn_de));
        btn_de.setOnClickListener(this);
        btn_fr = ((Button) findViewById(R.id.btn_fr));
        btn_fr.setOnClickListener(this);
        txt_hello = (TextView) findViewById(R.id.txt_hello);


        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(afChangeListener,            // Use the music stream.
                 AudioManager.STREAM_RING,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // Start playback.
        }

    }


    private void addButtons(){
        LinearLayout container = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        container.setLayoutParams(layoutParams);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setBackgroundColor(Color.BLUE);

        int count = 10;
        for(int i = 0; i < count; i++){
            Button button= new Button(this);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            button.setLayoutParams(layoutParams2);
            button.setText("Button " + i);
            container.addView(button);
        }

        this.container.addView(container);
    }
    private class MyListItem extends BaseAnswer{

        public String name;

        public int index;

        public MyListItem(int index){
            this.index = index;
            this.name = "MyListItem " + index;
        }

    }

    @Override
    public void onClick(View v) {
        String lang = "en";
        switch (v.getId()){
            case R.id.start_button:
//                chronometer.setBase(SystemClock.elapsedRealtime());
//                chronometer.start();
                break;
            case R.id.stop_button:
                chronometer.stop();
                showElapsedTime();
                break;
            case R.id.btn_en:
                lang = "en";
                break;
            case R.id.btn_ru:
                lang = "ru";
                break;
            case R.id.btn_de:
                lang = "de";
                break;
            case R.id.btn_fr:
                lang = "fr";
                break;
            default:
                break;
        }
        LocaleHelper.setLocale(this, lang);
//        recreate();
//        Intent mIntent = getIntent();
//        finish();
//        startActivity(mIntent);
//        updateTexts();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void updateTexts()
    {
        txt_hello.setText(R.string.hello_world);
        btn_en.setText(R.string.btn_en);
        btn_ru.setText(R.string.btn_ru);
        btn_fr.setText(R.string.btn_fr);
        btn_de.setText(R.string.btn_de);
    }

    private void showElapsedTime() {
        long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
        Toast.makeText(this, "Elapsed seconds: " + (elapsedMillis / 1000), Toast.LENGTH_SHORT).show();
    }
}
