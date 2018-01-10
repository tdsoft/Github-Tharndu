package tdsolutions.com.findmylocation;

import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback, OnAzimuthChangedListener, OnLocationChangedListener {
    private Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    private boolean isCameraviewOn = false;
    private AugmentedPOI mPoi;

    private double mAzimuthReal = 0;
//    private double mAzimuthTeoretical = 0;
    private static double AZIMUTH_ACCURACY = 5;
    private double mMyLatitude = 0;
    private double mMyLongitude = 0;

    private MyCurrentAzimuth myCurrentAzimuth;
    private MyCurrentLocation myCurrentLocation;

    TextView descriptionTextView;
    ImageView pointerIcon;

    List<AugmentedPOI> augmentedPOIs = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setupListeners();
        setupLayout();
        setAugmentedRealityPoint();
    }

    private void setAugmentedRealityPoint() {
        if(augmentedPOIs.size() == 0) {
            augmentedPOIs.add(new AugmentedPOI(
                    "Havelock City",
                    "Havelock City, Havelock Road, Colombo",
                    6.882808, 79.866903
            ));

            augmentedPOIs.add(new AugmentedPOI(
                    "Savoy 3D Cinema ",
                    "Savoy 3D Cinema, Galle Road, Colombo",
                    6.879120, 79.859737
            ));
        }
    }


    public double calculateTeoreticalAzimuth(AugmentedPOI mPoi) {

//            this.mPoi = augmentedPOIs.pop();
        double dX = mPoi.getPoiLatitude() - mMyLatitude;
        double dY = mPoi.getPoiLongitude() - mMyLongitude;

        double phiAngle;
        double tanPhi;
        double azimuth = 0;

        tanPhi = Math.abs(dY / dX);
        phiAngle = Math.atan(tanPhi);
        phiAngle = Math.toDegrees(phiAngle);

        if (dX > 0 && dY > 0) { // I quater
            return azimuth = phiAngle;
        } else if (dX < 0 && dY > 0) { // II
            return azimuth = 180 - phiAngle;
        } else if (dX < 0 && dY < 0) { // III
            return azimuth = 180 + phiAngle;
        } else if (dX > 0 && dY < 0) { // IV
            return azimuth = 360 - phiAngle;
        }
        return phiAngle;
    }

    private List<Double> calculateAzimuthAccuracy(double azimuth) {
        double minAngle = azimuth - AZIMUTH_ACCURACY;
        double maxAngle = azimuth + AZIMUTH_ACCURACY;
        List<Double> minMax = new ArrayList<>();

        if (minAngle < 0)
            minAngle += 360;

        if (maxAngle >= 360)
            maxAngle -= 360;

        minMax.clear();
        minMax.add(minAngle);
        minMax.add(maxAngle);

        return minMax;
    }

    private boolean isBetween(double minAngle, double maxAngle, double azimuth) {
        if (minAngle > maxAngle) {
            if (isBetween(0, maxAngle, azimuth) && isBetween(minAngle, 360, azimuth))
                return true;
        } else {
            if (azimuth > minAngle && azimuth < maxAngle)
                return true;
        }
        return false;
    }

    private String lastPoint;
    private void updateDescription() {
        if(mPoi == null)
            return;

//        if(lastPoint!=null && !lastPoint.equals(mPoi.getPoiName())) {
            descriptionTextView.setText(mPoi.getPoiName() + " azimuthTeoretical "
                    + mPoi.getAzimuth() + " azimuthReal " + mAzimuthReal + " latitude "
                    + mMyLatitude + " longitude " + mMyLongitude);
//        }
        lastPoint = mPoi.getPoiName();
    }

    int finish = 0;

    @Override
    public void onLocationChanged(Location location) {
        mMyLatitude = location.getLatitude();
        mMyLongitude = location.getLongitude();
        for(AugmentedPOI augmentedPOI: augmentedPOIs) {
            augmentedPOI.setAzimuth(calculateTeoreticalAzimuth(augmentedPOI));
        }
        Toast.makeText(this,"latitude: "+location.getLatitude()+" longitude: "+location.getLongitude(), Toast.LENGTH_SHORT).show();
        updateDescription();
    }

    @Override
    public void onAzimuthChanged(float azimuthChangedFrom, float azimuthChangedTo) {
        mAzimuthReal = azimuthChangedTo;
        pointerIcon = (ImageView) findViewById(R.id.icon);


        for(AugmentedPOI augmentedPOI: augmentedPOIs) {
            augmentedPOI.setAzimuth(calculateTeoreticalAzimuth(augmentedPOI));


            augmentedPOI.minAngle = calculateAzimuthAccuracy(augmentedPOI.getAzimuth()).get(0);
            augmentedPOI.maxAngle = calculateAzimuthAccuracy(augmentedPOI.getAzimuth()).get(1);


            mPoi = augmentedPOI;
            if (isBetween(augmentedPOI.minAngle, augmentedPOI.maxAngle, mAzimuthReal)) {
                System.out.println("minAngle " + augmentedPOI.minAngle + " maxAngle " + augmentedPOI.maxAngle + " mAzimuthReal " + mAzimuthReal + " " + augmentedPOI.getPoiName());
                pointerIcon.setVisibility(View.VISIBLE);
            } else {
                pointerIcon.setVisibility(View.INVISIBLE);
            }
        }
        updateDescription();


    }

    @Override
    protected void onStop() {
        myCurrentAzimuth.stop();
        myCurrentLocation.stop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myCurrentAzimuth.start();
        myCurrentLocation.start();
    }

    private void setupListeners() {
        myCurrentLocation = new MyCurrentLocation(this);
        myCurrentLocation.buildGoogleApiClient(this);
        myCurrentLocation.start();

        myCurrentAzimuth = new MyCurrentAzimuth(this, this);
        myCurrentAzimuth.start();
    }

    private void setupLayout() {
        descriptionTextView = (TextView) findViewById(R.id.cameraTextView);

        getWindow().setFormat(PixelFormat.UNKNOWN);
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.cameraview);
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        if (isCameraviewOn) {
            mCamera.stopPreview();
            isCameraviewOn = false;
        }

        if (mCamera != null) {
            try {
                mCamera.setPreviewDisplay(mSurfaceHolder);
                mCamera.startPreview();
                isCameraviewOn = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        mCamera.setDisplayOrientation(90);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        isCameraviewOn = false;
    }

}
