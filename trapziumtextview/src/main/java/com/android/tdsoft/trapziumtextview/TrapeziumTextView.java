package com.android.tdsoft.trapziumtextview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by Admin on 1/7/2016.
 */
public class TrapeziumTextView extends TextView {
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
    public void onDraw(Canvas canvas) {
        drawBackground(canvas);
        super.onDraw(canvas);
    }

    private void drawBackground(Canvas canvas) {
        switch (backgroundTypes) {
            case CIRCLE:
                int x = getWidth() / 2;
                int y = getHeight() / 2;
                canvas.drawCircle(x, y, x, paint);
                break;
            case TRAPEZIUM:
                int h = getMeasuredHeight();
                int w = getMeasuredWidth();
                path = new Path();
                path.moveTo(0, 0);
                path.lineTo(0, h);
                int bottomWidth = (int) (w - dpToPx(getContext(), reducingAmount));
                System.out.println(getText() + " " + bottomWidth);
                path.lineTo(bottomWidth, h);
                path.lineTo(w, 0);
                path.lineTo(0, 0);
                canvas.drawPath(path, paint);
                break;

        }
    }

    private int backColor;
    private float reducingAmount;
    private BackgroundTypes backgroundTypes;

    public int getBackColor() {
        return backColor;
    }

    public void setBackColor(@ColorInt int backColor) {
        this.backColor = backColor;
        invalidate();
        requestLayout();
    }

    public enum BackgroundTypes {
        CIRCLE,
        TRAPEZIUM
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TrapeziumTextView);
            setBackColor(a.getColor(R.styleable.TrapeziumTextView_back_color, Color.BLACK));
            int enumVal = a.getInt(R.styleable.TrapeziumTextView_backgroundTypes, 1);
            backgroundTypes = BackgroundTypes.values()[enumVal];
            reducingAmount = a.getFloat(R.styleable.TrapeziumTextView_reducing_amount_from_bottom, 10f);
            a.recycle();
        } else {
            backgroundTypes = BackgroundTypes.TRAPEZIUM;
            setBackColor(Color.BLACK);
            reducingAmount = 10f;
        }


        paint = new Paint();
        paint.setColor(getBackColor());
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!isSet && backgroundTypes==BackgroundTypes.TRAPEZIUM){
            setPadding(getPaddingLeft(),getPaddingTop(), (int) (getPaddingRight() + dpToPx(getContext(),10)), getPaddingBottom());
        }
        isSet = true;
    }

    private static float dpToPx(Context context , float value){
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());
        return px;
    }

}
