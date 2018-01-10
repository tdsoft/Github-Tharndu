package com.android.tdsoft.uitestingnightclub;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Admin on 2/3/2016.
 */
public class MyEnumAnotCheck {
    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;


    @IntDef({SUNDAY, MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY,SATURDAY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WeekDays {}

    @WeekDays int currentDay = SUNDAY;

    public void selectTheDay(@WeekDays int currentDay){
        this.currentDay = currentDay;
    }

    @WeekDays
    public int getWeekDay(){
        return currentDay;
    }

}
