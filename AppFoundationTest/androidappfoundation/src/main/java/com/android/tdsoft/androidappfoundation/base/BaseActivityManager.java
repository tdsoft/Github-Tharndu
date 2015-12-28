package com.android.tdsoft.androidappfoundation.base;

/**
 * Created by Admin on 12/22/2015.
 */
public class BaseActivityManager {
    private static BaseActivityManager ourInstance;

    public synchronized static BaseActivityManager getInstance() {
        if(ourInstance==null){
            ourInstance = new BaseActivityManager();
        }
        return ourInstance;
    }

    private BaseActivityManager() {
        enableConnectionRetryDialog = true;
        listenToNetworkStatusChangesAlways = true;
        autoDismissRetryWhenNetworkAvailable = true;
        doNotCallAutoConnect = false;
        hideSoftKeyboardOnResume = true;
    }

    private boolean enableConnectionRetryDialog;
    private boolean listenToNetworkStatusChangesAlways;
    private boolean autoDismissRetryWhenNetworkAvailable;
    private boolean doNotCallAutoConnect;
    private boolean hideSoftKeyboardOnResume;



    public void setEnableConnectionRetryDialog(boolean enableConnectionRetryDialog) {
        this.enableConnectionRetryDialog = enableConnectionRetryDialog;
    }

    public void setListenToNetworkStatusChangesAlways(boolean listenToNetworkStatusChangesAlways) {
        this.listenToNetworkStatusChangesAlways = listenToNetworkStatusChangesAlways;
    }


    public boolean isEnableConnectionRetryDialog() {
        return enableConnectionRetryDialog;
    }

    public boolean isListenToNetworkStatusChangesAlways() {
        return listenToNetworkStatusChangesAlways;
    }

    public boolean isAutoDismissRetryWhenNetworkAvailable() {
        return autoDismissRetryWhenNetworkAvailable;
    }

    public void setAutoDismissRetryWhenNetworkAvailable(boolean autoDismissRetryWhenNetworkAvailable) {
        this.autoDismissRetryWhenNetworkAvailable = autoDismissRetryWhenNetworkAvailable;
    }

    public boolean isDoNotCallAutoConnect() {
        return doNotCallAutoConnect;
    }

    public void setDoNotCallAutoConnect(boolean doNotCallAutoConnect) {
        this.doNotCallAutoConnect = doNotCallAutoConnect;
    }

    public boolean isHideSoftKeyboardOnResume() {
        return hideSoftKeyboardOnResume;
    }

    public void setHideSoftKeyboardOnResume(boolean hideSoftKeyboardOnResume) {
        this.hideSoftKeyboardOnResume = hideSoftKeyboardOnResume;
    }

}
