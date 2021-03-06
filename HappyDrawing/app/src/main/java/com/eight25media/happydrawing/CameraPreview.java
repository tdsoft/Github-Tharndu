package com.eight25media.happydrawing;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by Admin on 3/1/2016.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "CameraPreview" ;
    // SurfaceHolder
    private SurfaceHolder mHolder;

    // Our Camera.
    public Camera mCamera;

    // Parent Context.
    private Context mContext;

    // Camera Sizing (For rotation, orientation changes)
    private Camera.Size mPreviewSize;

    // List of supported preview sizes
    private List<Camera.Size> mSupportedPreviewSizes;

    // Flash modes supported by this camera
    private List<String> mSupportedFlashModes;

    // View holding this camera.
    private View mCameraView;

    public CameraPreview(Context context, Camera camera) {
        super(context);

        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }



    /**
     * The Surface has been created, now tell the camera where to draw the preview.
     * @param holder
     */
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    /**
     * Dispose of the camera preview.
     * @param holder
     */
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * React to surface changed events
     * @param holder
     * @param format
     * @param w
     * @param h
     */
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            Camera.Parameters parameters = mCamera.getParameters();

            // Set the auto-focus mode to "continuous"
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            Camera.Size optimalSize = getOptimalPreviewSize(sizes, getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels);

            Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

            if (display.getRotation() == Surface.ROTATION_0) {
                parameters.setPreviewSize(optimalSize.height, optimalSize.width);
                mCamera.setDisplayOrientation(90);
            }

            if (display.getRotation() == Surface.ROTATION_90) {
                parameters.setPreviewSize(optimalSize.width, optimalSize.height);
            }

            if (display.getRotation() == Surface.ROTATION_180) {
                parameters.setPreviewSize(optimalSize.height, optimalSize.width);
            }

            if (display.getRotation() == Surface.ROTATION_270) {
                parameters.setPreviewSize(optimalSize.width, optimalSize.height);
                mCamera.setDisplayOrientation(180);
            }
                parameters.setPreviewSize(optimalSize.width, optimalSize.height);

            mCamera.setParameters(parameters);
            mCamera.startPreview();

            requestLayout();


        } catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     *
     * @param sizes
     * @param w
     * @param h
     * @return
     */
    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.05;
        double targetRatio = (double) w/h;

        if (sizes==null) return null;

        Camera.Size optimalSize = null;

        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Find size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }


}