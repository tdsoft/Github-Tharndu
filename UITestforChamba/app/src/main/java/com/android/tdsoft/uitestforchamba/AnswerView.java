package com.android.tdsoft.uitestforchamba;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

/**
 * Created by Admin on 2/10/2016.
 */
public class AnswerView extends LinearLayout {
    public AnswerView(Context context) {
        super(context);
        initView(context);
    }

    public AnswerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AnswerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.answer_view,this);
    }
}