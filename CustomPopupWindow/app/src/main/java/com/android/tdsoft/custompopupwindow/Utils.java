package com.android.tdsoft.custompopupwindow;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Admin on 4/27/2016.
 */
public class Utils {
    public static float getCorrectSize(Context context, float value) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());
        return px;
    }
}
