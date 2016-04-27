package com.android.tdsoft.custompopupwindow;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

/**
 * Created by Admin on 4/27/2016.
 */
public class CustomPopup {
    PopupWindow popupWindow;
    View anchor;

    public CustomPopup(Activity context, View anchor) {
        this.anchor = anchor;
        View listViewDogs = LayoutInflater.from(anchor.getContext()).inflate(R.layout.layout_popup, null);


        DisplayMetrics dislayMat = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dislayMat);
        // initialize a pop up window type
        popupWindow = new PopupWindow(listViewDogs);
        ImageView imageView = (ImageView) listViewDogs.findViewById(R.id.close);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        // some other visual settings
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth((int) (dislayMat.widthPixels * 0.5f));
        popupWindow.setHeight((int) (dislayMat.heightPixels * 0.3f));

        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

    }

    public CustomPopup show() {
        popupWindow.showAsDropDown(anchor, -(popupWindow.getWidth() / 2 - anchor.getWidth() / 2), (int) (0 - (popupWindow.getHeight() + anchor.getHeight() - Utils.getCorrectSize(anchor.getContext(),10))));
        return this;
    }

    public void dismiss() {
        if (popupWindow != null)
            popupWindow.dismiss();
    }
}
