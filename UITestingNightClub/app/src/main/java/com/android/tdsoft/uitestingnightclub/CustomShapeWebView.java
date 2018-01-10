package com.android.tdsoft.uitestingnightclub;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by Admin on 1/21/2016.
 */
public class CustomShapeWebView extends WebView {
    private static final int MODE_ADD = 0;
    private static final int MODE_CLEAR = 1;
    private static final int MODE_DARKEN = 2;
    private static final int MODE_DST = 3;
    private static final int MODE_DST_ATOP = 4;
    private static final int MODE_DST_IN = 5;
    private static final int MODE_DST_OUT = 6;
    private static final int MODE_DST_OVER = 7;
    private static final int MODE_LIGHTEN = 8;
    private static final int MODE_MULTIPLY = 9;
    private static final int MODE_OVERLAY = 10;
    private static final int MODE_SCREEN = 11;
    private static final int MODE_SRC = 12;
    private static final int MODE_SRC_ATOP = 13;
    private static final int MODE_SRC_IN = 14;
    private static final int MODE_SRC_OUT = 15;
    private static final int MODE_SRC_OVER = 16;
    private static final int MODE_XOR = 17;

    Paint paint;
    private Bitmap mCanvasBitmap;
    private Canvas mCanvas;
    private PorterDuffXfermode mXfermode;
    private Bitmap mMask;
    private Xfermode mPorterDuffXferMode;

    public CustomShapeWebView(Context context) {
        super(context);
        init();
    }

    public CustomShapeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomShapeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init() {
        paint = createPaint();
        mPorterDuffXferMode = getModeFromInteger(5);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            //Create an area to use for content rendering
            mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mCanvasBitmap);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mMask = makeBitmapMask(ContextCompat.getDrawable(getContext(), R.drawable.masking_masking_01));
        if (mCanvas != null && mMask != null) {
            paint.setXfermode(mPorterDuffXferMode);
            mCanvas.drawBitmap(mMask, 0.0f, 0.0f, paint);
            paint.setXfermode(null);
        }
        super.onDraw(mCanvas);


    }

    private Paint createPaint() {
        Paint output = new Paint(Paint.ANTI_ALIAS_FLAG);
        output.setXfermode(getModeFromInteger(5));
        return output;
    }

    private PorterDuffXfermode getModeFromInteger(int index) {
        PorterDuff.Mode mode = null;
        switch (index) {
            case MODE_ADD:
                if (Build.VERSION.SDK_INT >= 11) {
                    mode = PorterDuff.Mode.ADD;
                } else {
                    System.out.println("MODE_ADD is not supported on api lvl " + Build.VERSION.SDK_INT);
                }
            case MODE_CLEAR:
                mode = PorterDuff.Mode.CLEAR;
                break;
            case MODE_DARKEN:
                mode = PorterDuff.Mode.DARKEN;
                break;
            case MODE_DST:
                mode = PorterDuff.Mode.DST;
                break;
            case MODE_DST_ATOP:
                mode = PorterDuff.Mode.DST_ATOP;
                break;
            case MODE_DST_IN:
                mode = PorterDuff.Mode.DST_IN;
                break;
            case MODE_DST_OUT:
                mode = PorterDuff.Mode.DST_OUT;
                break;
            case MODE_DST_OVER:
                mode = PorterDuff.Mode.DST_OVER;
                break;
            case MODE_LIGHTEN:
                mode = PorterDuff.Mode.LIGHTEN;
                break;
            case MODE_MULTIPLY:
                mode = PorterDuff.Mode.MULTIPLY;
                break;
            case MODE_OVERLAY:
                if (Build.VERSION.SDK_INT >= 11) {
                    mode = PorterDuff.Mode.OVERLAY;
                } else {
                    System.out.println("MODE_OVERLAY is not supported on api lvl " + Build.VERSION.SDK_INT);
                }
            case MODE_SCREEN:
                mode = PorterDuff.Mode.SCREEN;
                break;
            case MODE_SRC:
                mode = PorterDuff.Mode.SRC;
                break;
            case MODE_SRC_ATOP:
                mode = PorterDuff.Mode.SRC_ATOP;
                break;
            case MODE_SRC_IN:
                mode = PorterDuff.Mode.SRC_IN;
                break;
            case MODE_SRC_OUT:
                mode = PorterDuff.Mode.SRC_OUT;
                break;
            case MODE_SRC_OVER:
                mode = PorterDuff.Mode.SRC_OVER;
                break;
            case MODE_XOR:
                mode = PorterDuff.Mode.XOR;
                break;
            default:
                mode = PorterDuff.Mode.DST_IN;
        }
        System.out.println("Mode is " + mode.toString());
        return new PorterDuffXfermode(mode);
    }

    @Nullable
    private Bitmap makeBitmapMask(@Nullable Drawable drawable) {
        if (drawable != null) {
            if (getMeasuredWidth() > 0 && getMeasuredHeight() > 0) {
                Bitmap mask = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mask);
                drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                drawable.draw(canvas);
                return mask;
            } else {
                System.out.println("Can't create a mask with height 0 or width 0. Or the layout has no children and is wrap content");
                return null;
            }
        } else {
            System.out.println("No bitmap mask loaded, view will NOT be masked !");
        }
        return null;
    }
}
