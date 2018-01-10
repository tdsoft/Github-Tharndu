package com.android.tdsoft.uitestingnightclub;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.devmarvel.creditcardentry.library.CardValidCallback;
import com.devmarvel.creditcardentry.library.CreditCard;
import com.devmarvel.creditcardentry.library.CreditCardForm;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final int SWIPE_DURATION = 250;
    private static final int MOVE_DURATION = 150;
    private ViewPager pager;
    private boolean mItemPressed;
    private boolean mSwiping;
    private float mAlpha = 1;
    private CountDownTimer countDownTimer;
    private SeekBar circularSeekbar;
    private int min = 1;
    private int seconds = 120;
    private TextView mTvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circularSeekbar = (SeekBar) findViewById(R.id.circularSeekbar);
        mTvTime = (TextView) findViewById(R.id.mTvTime);


        circularSeekbar.setMax(seconds);
        circularSeekbar.setProgress(seconds);
        circularSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    seconds = progress;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                startTimer(seconds * 1000);
            }
        });

        startTimer(seconds * 1000);

        CreditCardForm creditCardForm = (CreditCardForm) findViewById(R.id.credit_card_form);
        creditCardForm.setOnCardValidCallback(new CardValidCallback() {
            @Override
            public void cardValid(CreditCard card) {
                System.out.println(card.getCardNumber());
            }
        });


//        HeartbeatView myBackgroundView = (HeartbeatView) findViewById(R.id.myBack);
//        setContentView(R.layout.activity_main_two);

//        pager = (ViewPager) findViewById(R.id.pager);
//        pager.setAdapter(new MyPagerAdapter(this));
//        final float density = getResources().getDisplayMetrics().density;
//
//        CirclePageIndicator titleIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
//        titleIndicator.setViewPager(pager);

        ImageView img = (ImageView) findViewById(R.id.img);
        img.setOnTouchListener(new View.OnTouchListener() {
            float mDownY;
            private int mSwipeSlop = -1;

            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if (mSwipeSlop < 0) {
                    mSwipeSlop = ViewConfiguration.get(MainActivity.this).getScaledTouchSlop();
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mItemPressed) {
                            // Multi-item swipes not handled
                            return false;
                        }
                        mItemPressed = true;
                        mDownY = event.getY();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        mItemPressed = false;
                        break;
                    case MotionEvent.ACTION_MOVE: {
                        float y = event.getY() + v.getTranslationY();
                        float deltaY = y - mDownY;
                        if (!mSwiping) {
                            if (deltaY > mSwipeSlop) {
                                mSwiping = true;
                            }
                        }
                        if (mSwiping) {
                            if (mAlpha > 1) {
                                mAlpha = 1;
                            } else if (mAlpha < 0) {
                                mAlpha = 0;
                            }
                            v.setAlpha(mAlpha);
                            mAlpha = mAlpha - ( 1 * (deltaY / v.getWidth()));
                        }
                    }
                    break;
                    case MotionEvent.ACTION_UP: {
                        mItemPressed = false;
                    }
                    break;
                    default:
                        return false;
                }
                return true;
            }
        });

//        WebView webView = (WebView) findViewById(R.id.web);
//        webView.setInitialScale(1);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.setScrollbarFadingEnabled(false);
//        webView.setVerticalScrollBarEnabled(false);
//        webView.setHorizontalScrollBarEnabled(false);
//        webView.setBackgroundColor(0);
//        webView.loadUrl("https://www.google.lk/");
    }

    public void onNextPageClick(View view) {
        Intent intent = new Intent(this, MyParallax.class);
        startActivity(intent);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
//        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.wrap);
//        ViewTreeObserver vto = linearLayout.getViewTreeObserver();
//        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if(Build.VERSION.SDK_INT > 15)
//                    linearLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                else
//                    linearLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//
//                int width = linearLayout.getMeasuredWidth();
//                int height = linearLayout.getMeasuredHeight();
//                int itemCount = getResources().getInteger(R.integer.item_count);
//                recyclerView.setAdapter(new MyAdapter(height,itemCount));
//
//            }
//        });
//
//    }


    private void startTimer(final long milisecods) {
        if(countDownTimer!=null){
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(milisecods, 500) {

            // 500 means, onTick function will be called at every 500
            // milliseconds

            @Override
            public void onTick(long leftTimeInMilliseconds) {

                int barVal = (int)leftTimeInMilliseconds / 1000;
                circularSeekbar.setProgress(barVal);
                mTvTime.setText(String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(leftTimeInMilliseconds),
                        TimeUnit.MILLISECONDS.toSeconds(leftTimeInMilliseconds) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(leftTimeInMilliseconds))
                ));
            }

            @Override
            public void onFinish() {

            }
        }.start();

    }

}
