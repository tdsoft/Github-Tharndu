package com.android.tdsoft.androidappfoundation.base;

import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.tdsoft.androidappfoundation.R;
import com.android.tdsoft.androidappfoundation.base.callbacks.BaseOnClickListener;
import com.android.tdsoft.androidappfoundation.base.callbacks.OnAutoConnectListener;
import com.android.tdsoft.androidappfoundation.base.callbacks.OnNetworkConnectionListener;
import com.android.tdsoft.androidappfoundation.base.callbacks.OnRetryClickListener;
import com.android.tdsoft.androidappfoundation.base.data.ConnectionDetails;
import com.android.tdsoft.androidappfoundation.base.utils.AppUtils;
import com.android.tdsoft.androidappfoundation.base.utils.DialogUtil;
import com.android.tdsoft.androidappfoundation.base.utils.NetworkUtil;
import com.android.tdsoft.androidappfoundation.base.utils.SnackBarUtil;

import de.greenrobot.event.EventBus;

public class AppBaseActivity extends AppCompatActivity implements OnNetworkConnectionListener, OnRetryClickListener, OnAutoConnectListener {


    private static final String TAG = "AppBaseActivity";
    private CoordinatorLayout mCoordinatorLayout;
    private Snackbar retrySnack;
    private AlertDialog retryAlertDialog;
    private int container;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (BaseActivityManager.getInstance().isHideSoftKeyboardOnResume()) {
            hideKeyboard();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }


    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    protected final void registerCoordinatorLayout(CoordinatorLayout coordinator_layout) {
        this.mCoordinatorLayout = coordinator_layout;
    }

    protected final void registerFragmentContainer(int container) {
        this.container = container;
    }

    protected void replaceFragment(AppBaseFragment fragment, boolean addToBackStack) {
        if (container < 0) {
            Log.d(TAG, "Container not set");
            return;
        }
        if (fragment != null) {
            hideKeyboard();
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            if (addToBackStack) {
                getSupportFragmentManager().beginTransaction().replace(this.container, fragment, fragment.getTagName()).addToBackStack(null).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(this.container, fragment, fragment.getTagName()).commit();
            }
        }
    }

    protected final void hideKeyboard() {
        AppUtils.hideKeyboard(this);
    }

    @Override
    public void onEventMainThread(ConnectionDetails connectionDetails) {
        if (!BaseActivityManager.getInstance().isListenToNetworkStatusChangesAlways()) {
            return;
        }

        if (!connectionDetails.isConnected()) {
            showRetryDialog(getString(R.string.no_connection));
        } else {
            if (BaseActivityManager.getInstance().isAutoDismissRetryWhenNetworkAvailable()) {
                dismissRetry();
            }
            if (!BaseActivityManager.getInstance().isDoNotCallAutoConnect()) {
                onAutoConnect();
            }
        }
    }

    protected void showRetryDialog(CharSequence message) {
        if (BaseActivityManager.getInstance().isEnableConnectionRetryDialog() == false)
            return;

        if (mCoordinatorLayout != null) {
            retrySnack = SnackBarUtil.showSnackLong(mCoordinatorLayout, message, getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRetryClick();
                }
            });
        } else {
            retryAlertDialog = DialogUtil.showRetryDialog(this, null, message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onRetryClick();
                }
            });
        }
    }

    protected void showRetryDialog(CharSequence message, final BaseOnClickListener baseOnClickListener) {
        if (mCoordinatorLayout != null) {
            retrySnack = SnackBarUtil.showSnackLong(mCoordinatorLayout, message, getString(R.string.retry), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    baseOnClickListener.onClick();
                }
            });
        } else {
            retryAlertDialog = DialogUtil.showRetryDialog(this, null, message, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    baseOnClickListener.onClick();
                }
            });
        }
    }

    protected void dismissRetry() {
        if (retrySnack != null)
            retrySnack.dismiss();
        if (retryAlertDialog != null) {
            retryAlertDialog.dismiss();
        }
    }

    @Override
    public void onRetryClick() {
        System.out.println("onRetryClick");
        dismissRetry();
        checkConnection();


        if (!hasFragments()) {
            return;
        }

        //notify fragments
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null && fragment instanceof AppBaseFragment && fragment instanceof OnRetryClickListener) {
                ((AppBaseFragment) fragment).onRetryClick();
            }
        }
    }

    protected void checkConnection() {
        if (!NetworkUtil.getConnectivity(this).isConnected()) {
            showRetryDialog(getString(R.string.no_connection));
        }
    }

    public boolean hasFragments() {
        return fragmentManager.getFragments() != null && fragmentManager.getFragments().size() > 0;
    }

    @Override
    public void onAutoConnect() {
        System.out.println("autoConnect");


        if (!hasFragments()) {
            return;
        }

        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null && fragment instanceof AppBaseFragment && fragment instanceof OnRetryClickListener) {
                ((AppBaseFragment) fragment).onAutoConnect();
            }
        }
    }
}
