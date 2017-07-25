package tdsoft.com.customcamera;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    private int mCamId;
    private Camera.CameraInfo mCurrentCameraInfo;
    private Camera mCamera;
    private Camera.Size mOptimalCameraPreviewSize;
    private boolean mTurnFlash;
    private TextureView mTextureView;
    private CameraSurfaceTextureListener mCameraSurfaceTextureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        mCameraSurfaceTextureListener = new CameraSurfaceTextureListener();
        mTextureView = (TextureView) findViewById(R.id.texture_view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startCam();
                } else {
                    System.exit(0);
                }
                break;
        }
    }

    class CameraSurfaceTextureListener implements TextureView.SurfaceTextureListener {

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            releaseCam();
            return true;
        }

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d("!!!!", "onSurfaceTextureAvailable!!!");
            setupCamera(surface, width, height);
        }
    }

    private void setupCamera(SurfaceTexture surface, int width, int height) {
        Pair<Camera.CameraInfo, Integer> backCamera = getCamera(mCamId);
        mCurrentCameraInfo = backCamera.first;
        mCamera = Camera.open(mCamId);
        cameraDisplayRotation();

        try {
            mCamera.setPreviewTexture(surface);
            mCamera.startPreview();
        } catch (IOException ioe) {
            // Something bad happened
        }

        if (mCamera == null) {
            return;
        }


        Camera.Parameters parameters = mCamera.getParameters();
        mOptimalCameraPreviewSize = getOptimalPreviewSize(parameters.getSupportedPreviewSizes(), width, height);
        parameters.setPreviewSize(mOptimalCameraPreviewSize.width, mOptimalCameraPreviewSize.height);

        if (parameters.getSupportedPictureSizes().size() > 0) {
            mOptimalCameraPreviewSize = parameters.getSupportedPictureSizes().get(0);
            parameters.setPictureSize(mOptimalCameraPreviewSize.width, mOptimalCameraPreviewSize.height);
        }


        List<String> supportedFocusModes = parameters.getSupportedFocusModes();
        if (supportedFocusModes != null && supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        List<String> supportedSceneModes = parameters.getSupportedSceneModes();
        if (supportedSceneModes != null && supportedSceneModes.contains(Camera.Parameters.SCENE_MODE_AUTO)) {
            parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
        }

        List<String> supportedWhiteBalanceModes = parameters.getSupportedWhiteBalance();
        if (supportedWhiteBalanceModes != null && supportedWhiteBalanceModes.contains(Camera.Parameters.WHITE_BALANCE_AUTO)) {
            parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
        }

        parameters.setExposureCompensation(0);

        List<Integer> supportedImageFormats = parameters.getSupportedPictureFormats();
        if (supportedImageFormats != null && supportedImageFormats.contains(ImageFormat.JPEG)) {
            parameters.setPictureFormat(ImageFormat.JPEG);
        }

        parameters.setJpegQuality(100);
        mCamera.setParameters(parameters);

    }

    private void releaseCam() {
        if (mCamera != null) {
            mTurnFlash = false;
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private void startCam() {
        if (mTextureView.isAvailable()) {
            setupCamera(mTextureView.getSurfaceTexture(), mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mCameraSurfaceTextureListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCam();
    }

    private int cameraDisplayRotation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        if (mCurrentCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
            rotation = (mCurrentCameraInfo.orientation + degrees) % 330;
            rotation = (360 - rotation) % 360;

            if (rotation > 90 && rotation <= 180) {
                rotation = 180;
            } else if (rotation > 180 && rotation <= 360) {
                rotation = 0;
            }

        } else {
            // Back-facing
            rotation = (mCurrentCameraInfo.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(rotation);

        return degrees;
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int width, int height) {

        List<Camera.Size> collectorSizes = new ArrayList<>();

        for (Camera.Size size : sizes) {
            if (width > height) {
                if (size.width > width && size.height > height) {
                    collectorSizes.add(size);
                }
            } else {
                if (size.width > height && size.height > width) {
                    collectorSizes.add(size);
                }
            }
        }

        if (collectorSizes.size() > 0) {
            return Collections.min(collectorSizes, new Comparator<Camera.Size>() {
                @Override
                public int compare(Camera.Size lhs, Camera.Size rhs) {
                    return Long.signum(lhs.width * lhs.height - rhs.width * rhs.height);
                }
            });
        }
        return sizes.get(0);
    }

    private Pair<Camera.CameraInfo, Integer> getCamera(int facing) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        final int numberOfCameras = Camera.getNumberOfCameras();

        for (int i = 0; i < numberOfCameras; ++i) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == facing) {
                return new Pair<>(cameraInfo, i);
            }
        }
        return null;
    }

    private void checkPermissions() {
        //first check permission has granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startCam();
        } else {
            //otherwise send a request to user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_CAMERA);
        }
    }

    public void onFlashClick(View view) {

        if (mCamera != null) {

            mTurnFlash = mTurnFlash ? false : true;
            Camera.Parameters p = mCamera.getParameters();
            if (mTurnFlash) {
                p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            } else {
                p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            }

            mCamera.setParameters(p);
        }
    }

    public void onSwitchCameraClick(View view) {

        int tempCamId = (mCamId == Camera.CameraInfo.CAMERA_FACING_BACK ? Camera.CameraInfo.CAMERA_FACING_FRONT : Camera.CameraInfo.CAMERA_FACING_BACK);
        if (hasCamera(tempCamId)) {
            mCamId = tempCamId;
            releaseCam();
            startCam();
            return;
        }

        Toast.makeText(this, "Requested camera is not available", Toast.LENGTH_SHORT).show();
    }

    private boolean hasCamera(int facing) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        final int numberOfCameras = Camera.getNumberOfCameras();

        for (int i = 0; i < numberOfCameras; ++i) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == facing) {
                return true;
            }
        }
        return false;
    }

    public void onCaptureClick(View view) {
        takeImage();
    }

    private void takeImage() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {

            private File imageFile;

            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    // convert byte array into bitmap
                    Bitmap rotatedBitmap = CompressImage.compressImage(data, getRotation());
                    String state = Environment.getExternalStorageState();
                    File folder = null;
                    if (state.contains(Environment.MEDIA_MOUNTED)) {
                        folder = new File(Environment
                                .getExternalStorageDirectory() + "/Demo");
                    } else {
                        folder = new File(Environment
                                .getExternalStorageDirectory() + "/Demo");
                    }

                    boolean success = true;
                    if (!folder.exists()) {
                        success = folder.mkdirs();
                    }
                    if (success) {
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        imageFile = new File(folder.getAbsolutePath()
                                + File.separator
                                + timeStamp
                                + "Image.jpg");

                        imageFile.createNewFile();

                        Toast.makeText(MainActivity.this, "Success, Taken an image", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Image Not saved", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ByteArrayOutputStream ostream = new ByteArrayOutputStream();

                    // save image into gallery
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);

                    FileOutputStream fout = new FileOutputStream(imageFile);
                    fout.write(ostream.toByteArray());
                    fout.close();
                    ContentValues values = new ContentValues();

                    values.put(MediaStore.Images.Media.DATE_TAKEN,
                            System.currentTimeMillis());
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    values.put(MediaStore.MediaColumns.DATA,
                            imageFile.getAbsolutePath());

                    getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private int getRotation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;

            default:
                break;
        }

        if (mCurrentCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
            rotation = (mCurrentCameraInfo.orientation + degree) % 330;
            rotation = (360 - rotation) % 360;
            if (rotation > 90 && rotation <= 180) {
                rotation = 180;
            } else if (rotation > 180 && rotation <= 360) {
                rotation = 0;
            } else if (rotation == 90) {
                rotation = -90;
            }
        } else {
            // Back-facing
            rotation = (mCurrentCameraInfo.orientation - degree + 360) % 360;
        }
        return rotation;
    }

}
