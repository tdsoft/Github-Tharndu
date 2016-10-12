package com.tdsolutions.placereminder.data;

import android.support.annotation.IntDef;

import java.sql.Timestamp;

/**
 * Created by Admin on 10/12/2016.
 */

public class ReminderHeader {
    public static final int ENTER = 0;
    public static final int LEAVE = 1;
    public static final int BOTH = 2;

    @IntDef({ENTER, LEAVE,BOTH})
    public @interface NotifyType{}

    private int reminderHeaderId;
    private String headerTitle;
    private String headerDescription;
    private @NotifyType int notifyType;
    private long createdDate;


    public int getReminderHeaderId() {
        return reminderHeaderId;
    }

    public void setReminderHeaderId(int reminderHeaderId) {
        this.reminderHeaderId = reminderHeaderId;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getHeaderDescription() {
        return headerDescription;
    }

    public void setHeaderDescription(String headerDescription) {
        this.headerDescription = headerDescription;
    }

    public @NotifyType int getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(@NotifyType int notifyType) {
        this.notifyType = notifyType;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }



}
