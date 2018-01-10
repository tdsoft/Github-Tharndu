package com.android.tdsoft.uitestingnightclub;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Admin on 1/6/2016.
 */
public class MyView extends LinearLayout {
    private Paint paint;
    private Path mPath;

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawPath(mPath, paint);

        requestLayout();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xffff0000);
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPath.moveTo(200, 200);

        mPath.lineTo(200, 500);

        mPath.lineTo(400, 500);

        mPath.lineTo(400, 200);
    }
}
