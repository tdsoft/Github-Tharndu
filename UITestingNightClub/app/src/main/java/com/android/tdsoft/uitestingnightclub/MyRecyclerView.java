package com.android.tdsoft.uitestingnightclub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Admin on 12/29/2015.
 */
public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int widthSize = MeasureSpec.getSize(widthSpec);
        int heightSize = MeasureSpec.getSize(heightSpec);

        setMeasuredDimension(widthSize, heightSize);
    }
}
