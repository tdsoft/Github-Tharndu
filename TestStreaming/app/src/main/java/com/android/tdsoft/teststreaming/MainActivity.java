package com.android.tdsoft.teststreaming;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener {
    private static final String URL1 = "http://s6.voscast.com:10640/";
//    private static final String URL1 = "https://drive.google.com/file/d/0B3fGFYIvIG-SZU1zbGxHVlNvOGs";
    MediaPlayer media;
    private ProgressDialog loadingDialog;
    AlertDialog alertDialog;
    private TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingDialog = new ProgressDialog(this);
        loadingDialog.setMessage("Preparing");
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        txtInfo.setText("Stopped");
    }

    private void setStatus(boolean isPlaying) {
        if (isPlaying) {
            txtInfo.setText("Playing...");
        } else {
            txtInfo.setText("Paused");
        }
    }

    public void onPlayClick(View view) {
        try {
            if (media == null) {
                loadingDialog.show();
                media = new MediaPlayer();
                media.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
                media.setAudioStreamType(AudioManager.STREAM_MUSIC);
                media.setDataSource(URL1);
                media.setOnErrorListener(this);
                media.setOnPreparedListener(this);
                media.setOnInfoListener(this);
                media.prepareAsync();
                return;
            }

            if(!media.isPlaying()) {
                media.start();
                setStatus(true);
            }


        } catch (Exception e) {
            //Getting Exception
            e.printStackTrace();
        }
    }

    public void onPauseClick(View view) {
        loadingDialog.dismiss();
        if(media.isPlaying()){
            media.pause();
            setStatus(false);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(media!=null && !media.isPlaying()){
            media.start();
        }
    }

    @Override
    public void onPrepared(MediaPlayer media) {
        loadingDialog.dismiss();
        setStatus(true);
        if(media!=null) {
            media.start();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        loadingDialog.dismiss();
        setStatus(false);
        return false;
    }


    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                loadingDialog.show();
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                loadingDialog.dismiss();
                break;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        if (media != null) {
            media.stop();
            media.release();
            media = null;
        }
    }
}
