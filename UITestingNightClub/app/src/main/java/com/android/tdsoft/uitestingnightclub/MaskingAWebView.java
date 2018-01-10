package com.android.tdsoft.uitestingnightclub;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by Admin on 1/21/2016.
 */
public class MaskingAWebView extends WebView {
    public MaskingAWebView(Context context) {
        super(context);
        inti();
    }

    public MaskingAWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inti();
    }

    public MaskingAWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inti();
    }
    final Path path = new Path();
    private void inti(){
        path.addRoundRect(new RectF(0,0,getWidth(),getHeight()),10,10, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.clipPath(path);
        super.onDraw(canvas);
    }


}
