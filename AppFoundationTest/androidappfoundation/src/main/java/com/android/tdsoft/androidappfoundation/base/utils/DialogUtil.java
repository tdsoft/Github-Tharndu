package com.android.tdsoft.androidappfoundation.base.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.android.tdsoft.androidappfoundation.R;

/**
 * Created by Tharindu on 12/21/2015.j
 */
public class DialogUtil {

    public static AlertDialog getAlertDialog(Context context,String positiveButtonText, String negativeButtonText,
                                         DialogInterface.OnClickListener onPositiveClickListener, DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        if(positiveButtonText != null && positiveButtonText.length() > 0) {
            alertDialog.setPositiveButton(positiveButtonText, onPositiveClickListener);
        }

        if(negativeButtonText != null && negativeButtonText.length() > 0) {
            alertDialog.setNegativeButton(negativeButtonText, onNegativeClickListener);
        }
        return alertDialog.create();
    }
    public static AlertDialog getAlertDialog(Context context,int positiveButtonText, int negativeButtonText,
                                             DialogInterface.OnClickListener onPositiveClickListener, DialogInterface.OnClickListener onNegativeClickListener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        if(positiveButtonText > -1){
            alertDialog.setPositiveButton(positiveButtonText, onPositiveClickListener);
        }

        if(negativeButtonText > -1) {
            alertDialog.setNegativeButton(negativeButtonText, onNegativeClickListener);
        }
        return alertDialog.create();
    }

    public static AlertDialog showRetryDialog(Context context,CharSequence title, CharSequence message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = getAlertDialog(context, R.string.retry, -1, onClickListener, null);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.show();
        return alertDialog;
    }
}
