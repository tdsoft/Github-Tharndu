package com.android.tdsoft.uitestforchamba;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by Admin on 2/9/2016.
 */
public class AnswerController extends LinearLayout {
    public static final int SINGLE_CHOICE = 0;
    public static final int MULTIPLE_CHOICE = 1;

    @IntDef({SINGLE_CHOICE, MULTIPLE_CHOICE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ChoiceType {
    }

    private static final String TAG = "AnswerController";


    private int mMaxColumnCount = 2;
    private List itemList;
    private
    @ChoiceType
    int mChoiceType;


    public void setMaxColumnCount(int maxColumnCount) {
        this.mMaxColumnCount = maxColumnCount;
    }

    public void setChoiceType(@ChoiceType int choiceType) {
        this.mChoiceType = choiceType;
    }

    public void setItemList(List itemList) {
        this.itemList = itemList;
    }


    public AnswerController(Context context) {
        super(context);
        initViews(context);
    }

    public AnswerController(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public AnswerController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        setLayoutParams(params);
        setBackgroundColor(Color.BLUE);


        LinearLayout container = new LinearLayout(context);
        container.setId(R.id.cc_entry_internal);
        LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        container.setLayoutParams(containerParams);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setBackgroundColor(Color.RED);
        addView(container);
//        int count = 4;
//        for(int i = 0; i < count; i++){
//            Button button= new Button(getContext());
//            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
//            button.setLayoutParams(layoutParams2);
//            button.setText("Button " + i);
//            container.addView(button);
//        }
//        Button button = new Button(getContext());
//        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
//        button.setLayoutParams(layoutParams2);
//        button.setText("Button ");
//        addView(button);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void addButtons() {
        LinearLayout container = new LinearLayout(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        container.setLayoutParams(layoutParams);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setBackgroundColor(Color.BLUE);

        int count = 10;
        for (int i = 0; i < count; i++) {
            Button button = new Button(getContext());
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            button.setLayoutParams(layoutParams2);
            button.setText("Button " + i);
            container.addView(button);
        }

        this.addView(container);
    }

    private int getRowCount() {
        int count = 0;

        if (itemList == null || itemList.size() == 0) {
            return count;
        }

        if (mMaxColumnCount == 0) {
            Log.d(TAG, "Please provide max column count");
            return count;
        }

        int listSize = itemList.size();
        if (AnswerControlUtils.isEven(listSize)) {
            count = listSize / mMaxColumnCount;
        } else {
            count = (listSize / mMaxColumnCount) + 1;
        }
        return count;

    }
}
