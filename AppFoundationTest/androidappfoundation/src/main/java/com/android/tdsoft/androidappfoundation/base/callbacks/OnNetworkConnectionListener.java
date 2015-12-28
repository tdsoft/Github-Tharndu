package com.android.tdsoft.androidappfoundation.base.callbacks;

import com.android.tdsoft.androidappfoundation.base.AppBaseListener;
import com.android.tdsoft.androidappfoundation.base.data.ConnectionDetails;

/**
 * Created by Admin on 12/21/2015.
 */
public interface OnNetworkConnectionListener extends AppBaseListener {
    void onEventMainThread(ConnectionDetails connectionDetails);
}
