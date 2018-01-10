package tdsolutions.com.findmylocation;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

/**
 * Created by Admin on 1/6/2017.
 */

public class MyAzumathCurrentUsingAcceloMeter {
    private final OnAzimuthChangedListener mAzimuthListener;
    private final Context mContext;
    private int azimuthFrom = 0;
    private int azimuthTo = 0;

    private SensorManager mSensorManager = null;


    private Sensor mGravity;
    private Sensor mAccelerometer;
    private Sensor mMagnetometer;
    private Sensor mRotationVector;

    boolean haveRotationVector = false;
    boolean haveGravity = false;
    boolean haveAccelerometer = false;
    boolean haveMagnetometer = false;

    public MyAzumathCurrentUsingAcceloMeter(OnAzimuthChangedListener azimuthListener, Context context) {
        this.mAzimuthListener = azimuthListener;
        this.mContext = context;
    }

    public void start() {
        mSensorManager = (SensorManager) mContext.getSystemService(mContext.SENSOR_SERVICE);
        this.mGravity = this.mSensorManager.getDefaultSensor( Sensor.TYPE_GRAVITY );
        this.haveGravity = this.mSensorManager.registerListener( mSensorEventListener, this.mGravity, SensorManager.SENSOR_DELAY_GAME );

        this.mAccelerometer = this.mSensorManager.getDefaultSensor( Sensor.TYPE_ACCELEROMETER );
        this.haveAccelerometer = this.mSensorManager.registerListener( mSensorEventListener, this.mAccelerometer, SensorManager.SENSOR_DELAY_GAME );

        this.mMagnetometer = this.mSensorManager.getDefaultSensor( Sensor.TYPE_MAGNETIC_FIELD );
        this.haveMagnetometer = this.mSensorManager.registerListener( mSensorEventListener, this.mMagnetometer, SensorManager.SENSOR_DELAY_GAME );


        this.mRotationVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        this.haveMagnetometer = this.mSensorManager.registerListener( mSensorEventListener, this.mRotationVector, SensorManager.SENSOR_DELAY_UI );


        if(haveMagnetometer){
            this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mGravity);
            this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mMagnetometer);
            this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mAccelerometer);
        }
        // if there is a gravity sensor we do not need the accelerometer
        if( this.haveGravity ) {
            this.mSensorManager.unregisterListener(this.mSensorEventListener, this.mAccelerometer);
        }

        if ( ( haveGravity || haveAccelerometer ) && haveMagnetometer ) {
            Toast.makeText(mContext, "sensor available", Toast.LENGTH_SHORT).show();
        } else {
            // unregister and stop
            Toast.makeText(mContext, "Sensor not available", Toast.LENGTH_SHORT).show();
        }
    }

    private SensorEventListener mSensorEventListener = new SensorEventListener() {

        float[] gData = new float[3]; // gravity or accelerometer
        float[] mData = new float[3]; // magnetometer
        float[] rMat = new float[9];
        float[] iMat = new float[9];
        float[] orientation = new float[3];

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            azimuthFrom = azimuthTo;
            float[] data;
            switch (event.sensor.getType()) {
                case Sensor.TYPE_GRAVITY:
                    gData = event.values.clone();
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    gData = event.values.clone();
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    mData = event.values.clone();
                    break;
                default:
                    return;
            }


            if( event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR ){
                SensorManager.getRotationMatrixFromVector(rMat, event.values);
                azimuthTo = (int) ( Math.toDegrees( SensorManager.getOrientation( rMat, orientation )[0] ) + 360 ) % 360;

            }else{
                if ( SensorManager.getRotationMatrix( rMat, iMat, gData, mData ) ) {
                    azimuthTo= (int) ( Math.toDegrees( SensorManager.getOrientation( rMat, orientation )[0] ) + 360 ) % 360;
                }
            }
            mAzimuthListener.onAzimuthChanged(azimuthFrom, azimuthTo);
        }
    };


    public void stop() {
        mSensorManager.unregisterListener(mSensorEventListener, mGravity);
        mSensorManager.unregisterListener(mSensorEventListener, mAccelerometer);
        mSensorManager.unregisterListener(mSensorEventListener, mMagnetometer);
    }
}
