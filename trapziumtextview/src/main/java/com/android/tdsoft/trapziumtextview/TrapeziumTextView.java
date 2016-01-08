package com.android.tdsoft.trapziumtextview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by Admin on 1/7/2016.
 */
public class TrapeziumTextView extends TextView{
    private Path path;
    private Paint paint;
    private boolean isSet;

    public TrapeziumTextView(Context context) {
        super(context);
        init(context, null);
    }

    public TrapeziumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TrapeziumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    @Override
    public void draw(Canvas canvas) {
        drawBackground(canvas);
        super.draw(canvas);
        invalidate();
    }

    private void drawBackground(Canvas canvas) {
        switch (backgroundTypes) {
            case CIRCLE:
                int x = getWidth() / 2;
                int y = getHeight() / 2;
                canvas.drawCircle(x, y, x, paint);
                break;
            case RECTANGLE_WITH_TRIANGLE:
                canvas.drawPath(path, paint);
                break;

        }
    }

    private int backColor;
    private float reducingAmount;
    private BackgroundTypes backgroundTypes;

    public enum BackgroundTypes {
        CIRCLE,
        RECTANGLE_WITH_TRIANGLE
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TrapeziumTextView);
            backColor = a.getColor(R.styleable.TrapeziumTextView_back_color, Color.BLACK);
            int enumVal = a.getInt(R.styleable.TrapeziumTextView_backgroundTypes, 1);
            backgroundTypes = BackgroundTypes.values()[enumVal];
            reducingAmount = a.getFloat(R.styleable.TrapeziumTextView_reducing_amount_from_bottom,15f);
            a.recycle();
        } else {
            backgroundTypes = BackgroundTypes.RECTANGLE_WITH_TRIANGLE;
            backColor = Color.BLACK;
            reducingAmount = 15f;
        }

        path = new Path();
        paint = new Paint();
        paint.setColor(backColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!isSet && backgroundTypes==BackgroundTypes.RECTANGLE_WITH_TRIANGLE){
            setPadding(getPaddingLeft(),getPaddingTop(), (int) (getPaddingRight() + dpToPx(getContext(),10)), getPaddingBottom());
        }
        isSet = true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float with = w;
        path.moveTo(0, 0);
        path.lineTo(0, h);
        int bottomWidth = (int) (with - dpToPx(getContext(), reducingAmount));
        System.out.println(getText() + " " + bottomWidth);
        path.lineTo(bottomWidth, h);
        path.lineTo(with, 0);
        path.lineTo(0, 0);
    }

    private static float dpToPx(Context context , float value){
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());
        return px;
    }

}
