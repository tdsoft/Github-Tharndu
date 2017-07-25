package tdsoft.com.tdplayer;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.util.HashMap;
import java.util.Map;

import tdsoft.com.tdplayer.utils.Constants;

public class TDPlayerService extends Service implements View.OnTouchListener {
    private static final int STOP_SERVICE = 120;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private WindowManager mWindowManager;
    private View mFloatingView;
    WindowManager.LayoutParams params;
    private WebView youTubeView;

    public TDPlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_tdplayer, null, false);
        youTubeView = (WebView) mFloatingView.findViewById(R.id.youtube_view);
        youTubeView.requestFocus();
        youTubeView.setOnTouchListener(this);

        //Add the view to the window.
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null && intent.getAction().equals("STOP")) {
            stopForeground(true);
            stopSelf();
        } else {
            // Initializing video player with developer key
            youTubeView.getSettings().setJavaScriptEnabled(true);
            youTubeView.setWebChromeClient(new WebChromeClient(){

            });
            youTubeView.getSettings().setMediaPlaybackRequiresUserGesture(false);
            youTubeView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.2; Win64; x64; rv:21.0.0) Gecko/20121011 Firefox/21.0.0");
            Map hashMap = new HashMap();
            hashMap.put("Referer", "http://www.youtube.com");
            String url = intent.getStringExtra(Constants.EXTRA_URL).substring(intent.getStringExtra(Constants.EXTRA_URL).lastIndexOf("/") + 1);
            youTubeView.loadUrl("https://www.youtube.com/embed/" + url +"?autoplay=1", hashMap);
            setUpAsForeGround("Playing");
        }
        return START_NOT_STICKY;
    }

    Notification notification;

    private void setUpAsForeGround(String text) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);


        Intent stopIntent = new Intent(this, TDPlayerService.class);
        stopIntent.setAction("STOP");
        PendingIntent pendingStopIntent = PendingIntent.getService(this, STOP_SERVICE, stopIntent, 0);

        notification = new NotificationCompat.Builder(this).setContentTitle(getString(R.string.app_name))
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_action_stop, "Stop", pendingStopIntent)
                .build();

        startForeground(Notification.FLAG_ONGOING_EVENT, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //remember the initial position.
                initialX = params.x;
                initialY = params.y;

                //get the touch location
                initialTouchX = event.getRawX();
                initialTouchY = event.getRawY();
                return false;
            case MotionEvent.ACTION_MOVE:
                //Calculate the X and Y coordinates of the view.
                params.x = initialX + (int) (event.getRawX() - initialTouchX);
                params.y = initialY + (int) (event.getRawY() - initialTouchY);


                //Update the layout with new X & Y coordinate
                mWindowManager.updateViewLayout(mFloatingView, params);
                return false;
        }
        return false;
    }

}
