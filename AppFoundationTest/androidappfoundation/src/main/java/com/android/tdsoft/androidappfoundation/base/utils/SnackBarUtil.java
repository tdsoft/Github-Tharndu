package com.android.tdsoft.androidappfoundation.base.utils;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Admin on 12/22/2015.
 */
public class SnackBarUtil {
    public static Snackbar getSnackBar(View view, CharSequence message, int duration) {
        Snackbar snackbar = Snackbar.make(view, message, duration);
        return snackbar;
    }

    public static Snackbar getSnackBar(View view, @StringRes int resId, int duration) {
        Snackbar snackbar = Snackbar.make(view, resId, duration);
        return snackbar;
    }

    public static Snackbar showSnackShort(View view, CharSequence message, String buttonText, View.OnClickListener onClickListener) {
        Snackbar snackbar = getSnackBar(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setAction(buttonText, onClickListener);
        snackbar.show();
        return snackbar;
    }

    public static Snackbar showSnackMessage(View view, CharSequence message, @Snackbar.Duration int duration) {
        Snackbar snackbar = getSnackBar(view, message, duration);
        snackbar.show();
        return snackbar;
    }

    public static Snackbar showSnackMessage(View view, @StringRes int resId, @Snackbar.Duration int duration) {
        Snackbar snackbar = getSnackBar(view, resId, duration);
        snackbar.show();
        return snackbar;
    }

    public static Snackbar showSnackLong(View view, CharSequence message, String buttonText, View.OnClickListener onClickListener) {
        Snackbar snackbar = getSnackBar(view, message, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(buttonText, onClickListener);
        snackbar.show();
        return snackbar;
    }
}
