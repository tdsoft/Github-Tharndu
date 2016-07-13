package com.eight25media.happydrawing.callback;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by Admin on 3/2/2016.
 */
public interface OnImageTakenListener {
    void onImageTaken(File bitmap);
    void onImageTaken(Bitmap bitmap);
}
