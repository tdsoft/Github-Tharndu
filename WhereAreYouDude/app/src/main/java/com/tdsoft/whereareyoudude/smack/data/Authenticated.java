package com.tdsoft.whereareyoudude.smack.data;

/**
 * Created by Admin on 7/14/2016.
 */
public class Authenticated {
    private boolean isSuccess;

    public Authenticated(boolean isSuccess){
        this.isSuccess = isSuccess;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }
}
