package com.android.tdsoft.tdsliderdatepicker;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Admin on 1/8/2016.
 */
public class Utils {

    public static String toDateWithDaySuffix(Calendar calendar){
        String dateStr = "Not Available";
        if(calendar!=null) {
            int day = Integer.parseInt(dateToString(calendar, "dd"));
            dateStr = day + getDayOfMonthSuffix(day) + dateToString(calendar, " MMM yyyy").toUpperCase();
        }
        return dateStr;
    }

    private static String dateToString(Calendar calendar, String format) {
        DateFormat f = new SimpleDateFormat(format);
        if (calendar == null) {
            return "Not Available";
        }
        return f.format(calendar.getTime());
    }

    private static String getDayOfMonthSuffix(int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    public static float getSizeAccordingToDevice(Context context , float value){
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, r.getDisplayMetrics());
        return px;
    }

    public static DisplayMetrics getDeviceDisplayMetrics(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        //int actionBarHeight = UiUtil.getActionBarHeight(itemView.getContext());
        return displayMetrics;
    }
}
