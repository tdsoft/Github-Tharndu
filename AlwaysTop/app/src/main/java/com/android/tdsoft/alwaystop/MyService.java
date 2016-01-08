package com.android.tdsoft.alwaystop;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MyService extends Service implements View.OnTouchListener {
    private WindowManager mWindowManager;
    private DummyView mDummyView;
    HardwareKeyWatcher mHardwareKeyWatcher ;
    public MyService() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mDummyView = new DummyView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mDummyView.setLayoutParams(params);
        mDummyView.setOnTouchListener(this);

        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(params);
        imageView.setBackgroundResource(R.mipmap.ic_launcher);

        WindowManager.LayoutParams params2 = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, /* height */
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_FULLSCREEN|

                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.LEFT | Gravity.TOP;

        params2.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                |WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE

                |WindowManager.LayoutParams.FLAG_FULLSCREEN;


        mWindowManager.addView(mDummyView, params2);

        mDummyView.setBackgroundResource(R.drawable.aaa);


        mHardwareKeyWatcher = new HardwareKeyWatcher(this);
        mHardwareKeyWatcher.setOnHardwareKeysPressedListenerListener(new HardwareKeyWatcher.OnHardwareKeysPressedListener() {
            @Override
            public void onHomePressed() {
                System.out.println("onHomePressed");
               stopService(new Intent(MyService.this, MyService.class));
            }

            @Override
            public void onRecentAppsPressed() {
                System.out.println("onRecentAppsPressed");
                stopService(new Intent(MyService.this, MyService.class));
            }
        });
        mHardwareKeyWatcher.startWatch();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    private class DummyView extends LinearLayout{

        public DummyView(Context context) {
            super(context);
        }

        public DummyView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public DummyView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
            {
                // handle back press
                // if (event.getAction() == KeyEvent.ACTION_DOWN)
                return true;
            }
            return super.dispatchKeyEvent(event);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Toast.makeText(this,"onTouchEvent", Toast.LENGTH_LONG).show();
//        stopSelf();
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWindowManager.removeView(mDummyView);
        mHardwareKeyWatcher.stopWatch();
    }
}
