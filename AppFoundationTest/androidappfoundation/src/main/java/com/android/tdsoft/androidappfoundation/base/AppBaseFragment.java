package com.android.tdsoft.androidappfoundation.base;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.tdsoft.androidappfoundation.R;
import com.android.tdsoft.androidappfoundation.base.callbacks.OnAutoConnectListener;
import com.android.tdsoft.androidappfoundation.base.callbacks.OnRetryClickListener;
import com.android.tdsoft.androidappfoundation.base.utils.AppUtils;
import com.android.tdsoft.androidappfoundation.base.utils.DialogUtil;
import com.android.tdsoft.androidappfoundation.base.utils.NetworkUtil;
import com.android.tdsoft.androidappfoundation.base.utils.SnackBarUtil;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppBaseFragment extends Fragment implements OnAutoConnectListener, OnRetryClickListener {


    private Snackbar retrySnack;
    private AlertDialog retryAlertDialog;
    protected AppBaseActivity appBaseActivity;

    public AppBaseFragment() {
        // Required empty public constructor
    }

    public String getTagName() {
        return getTag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appBaseActivity = (AppBaseActivity) getActivity();
    }


    @Override
    public void onAutoConnect() {

    }

    @Override
    public void onRetryClick() {

    }
}
