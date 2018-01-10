package com.android.tdsoft.uitestingnightclub.anserview;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.tdsoft.uitestingnightclub.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Created by Admin on 2/9/2016.
 */
public class AnswerController extends LinearLayout {
    public static final int SINGLE_CHOICE = 0;
    public static final int MULTIPLE_CHOICE = 1;
    private LinearLayout container;

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
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.answer_view,this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addButtons();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    private void addButtons() {


//        int count = 10;
//        for (int i = 0; i < count; i++) {
//            Button button = new Button(getContext());
//            LayoutParams layoutParams2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
//            button.setLayoutParams(layoutParams2);
//            button.setText("Button " + i);
//            container.addView(button);
//        }


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
