package com.android.tdsoft.tdsliderdatepicker;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Admin on 1/8/2016.
 */
public class SliderDatePicker extends LinearLayout {
    private ImageView mPreviousButton;
    private ImageView mNextButton;
    private RecyclerView mDateList;
    private Calendar mDate, mTodayDate;
    private List<SliderDateItem> sliderDateItems = new ArrayList<>();
    private DisplayMetrics displayMetrics;
    private int DATE_ADD_COUNT = 30;

    private int STARTING_DATE_COUNT = DATE_ADD_COUNT + 5;
    private int STARTING_PRE_DATE_COUNT = 5;

    private OnScrollCompletedListener onScrollCompletedListener;

    public SliderDatePicker(Context context) {
        super(context);
        initViews(context);
    }

    public SliderDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public SliderDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.slider_datepicker, this);
        mDate = Calendar.getInstance();
        mTodayDate = Calendar.getInstance();
        sliderDateItems = refreshAndGetDates();

        displayMetrics = Utils.getDeviceDisplayMetrics(getContext());

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mNextButton = (ImageView) findViewById(R.id.btnNext);
        mNextButton = (ImageView) findViewById(R.id.btnBack);
        mDateList = (RecyclerView) findViewById(R.id.dateList);
        final SliderDateAdapter sliderDateAdapter = new SliderDateAdapter(sliderDateItems);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(HORIZONTAL);

        mDateList.setLayoutManager(layoutManager);
        mDateList.setAdapter(sliderDateAdapter);


        mDateList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int pos = -1;
                int offSet = (displayMetrics.widthPixels / 2 - widthOfChild / 2);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    if (scrolledWay == ScrolledWay.LEFT) {
                        pos = layoutManager.findLastCompletelyVisibleItemPosition();
                    } else if (scrolledWay == ScrolledWay.RIGHT) {
                        pos = layoutManager.findFirstCompletelyVisibleItemPosition();
                    }
                    System.out.println("Pos: " + pos);
                    if (pos == -1) {
                        pos = layoutManager.findLastVisibleItemPosition();
                    }
                    System.out.println((pos + 3) + " == " + sliderDateItems.size());

                    long startDateTimeMili = 0;
                    if (layoutManager.findLastVisibleItemPosition() == (sliderDateItems.size() - 1)) {
                        System.out.println("User scrolled to last");
                        startDateTimeMili = sliderDateItems.get(sliderDateItems.size() - 1).timeMillisecond;
                        addDate(startDateTimeMili, DATE_ADD_COUNT, 1);
                    }

                    if (layoutManager.findFirstVisibleItemPosition() == 0) {
                        System.out.println("User scrolled to first");
                        startDateTimeMili = sliderDateItems.get(0).timeMillisecond;
                        addDate(startDateTimeMili, DATE_ADD_COUNT, -1);
                        pos += DATE_ADD_COUNT;
                    }

                    layoutManager.scrollToPositionWithOffset(pos, offSet);
                    sliderDateAdapter.markCurrentSelectedItem(pos);
                    if (onScrollCompletedListener != null) {
                        onScrollCompletedListener.OnScrollCompleted(pos);
                    }
                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View child = recyclerView.findChildViewUnder(dx, dy);
                if (child == null)
                    return;

                widthOfChild = child.getWidth();

                //mark the first date
                if(dx== 0 && dy==0){
                    int offSet = (displayMetrics.widthPixels / 2 - widthOfChild / 2);
                    layoutManager.scrollToPositionWithOffset(9, offSet);
                    sliderDateAdapter.markCurrentSelectedItem(9);
                }

                if (dx > 0) {
                    scrolledWay = ScrolledWay.LEFT;
                    System.out.println("ScrolledWay.LEFT");
                } else if (dx < 0) {
                    scrolledWay = ScrolledWay.RIGHT;
                    System.out.println("ScrolledWay.RIGHT");
                }
            }
        });

    }

    private int widthOfChild;

    public void setOnScrollCompletedListener(OnScrollCompletedListener onScrollCompletedListener) {
        this.onScrollCompletedListener = onScrollCompletedListener;
    }

    public enum ScrolledWay {
        LEFT,
        RIGHT
    }

    private ScrolledWay scrolledWay;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private List<SliderDateItem> refreshAndGetDates() {
        List<SliderDateItem> dateLists = new ArrayList<>();
        for (int i = STARTING_PRE_DATE_COUNT; i > 0; i--) {
            mDate.add(Calendar.DAY_OF_MONTH, -1);
            String dateStr = Utils.toDateWithDaySuffix(mDate).toUpperCase();
            SliderDateItem sliderDateItem = new SliderDateItem();
            sliderDateItem.dateString = dateStr;
            sliderDateItem.timeMillisecond = mDate.getTimeInMillis();
            dateLists.add(sliderDateItem);
        }

        for (int i = 1; i <= STARTING_DATE_COUNT; i++) {
            mDate.add(Calendar.DAY_OF_MONTH, 1);
            String dateStr = Utils.toDateWithDaySuffix(mDate).toUpperCase();
            SliderDateItem sliderDateItem = new SliderDateItem();
            sliderDateItem.dateString = dateStr;
            sliderDateItem.timeMillisecond = mDate.getTimeInMillis();
            dateLists.add(sliderDateItem);
        }
        return dateLists;
    }


    private void addDate(long startDateTimeMile, int count, int increment) {
        mDate.setTimeInMillis(startDateTimeMile);
        for (int i = 1; i <= count; i++) {
            mDate.add(Calendar.DAY_OF_MONTH, increment);
            String dateStr = Utils.toDateWithDaySuffix(mDate).toUpperCase();
            SliderDateItem sliderDateItem = new SliderDateItem();
            sliderDateItem.dateString = dateStr;
            sliderDateItem.timeMillisecond = mDate.getTimeInMillis();
            if (increment > 0) {
                sliderDateItems.add(sliderDateItem);
            } else if (increment < 0) {
                sliderDateItems.add(0, sliderDateItem);
            }
        }
        mDateList.getAdapter().notifyDataSetChanged();
    }

    public interface OnScrollCompletedListener {
        void OnScrollCompleted(int pos);
    }


}
