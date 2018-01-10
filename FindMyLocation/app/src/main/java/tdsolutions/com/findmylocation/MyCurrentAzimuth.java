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

public class MyCurrentAzimuth implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private int azimuthFrom = 0;
    private int azimuthTo = 0;
    private OnAzimuthChangedListener mAzimuthListener;
    Context mContext;

    public MyCurrentAzimuth(OnAzimuthChangedListener azimuthListener, Context context) {
        mAzimuthListener = azimuthListener;
        mContext = context;
    }

    public void start(){
        sensorManager = (SensorManager) mContext.getSystemService(mContext.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);


       if(!sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI)){
           Toast.makeText(mContext, "Sensor not available", Toast.LENGTH_SHORT).show();
        }
    }

    public void stop(){
        sensorManager.unregisterListener(this);
    }

    public void setOnShakeListener(OnAzimuthChangedListener listener) {
        mAzimuthListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        azimuthFrom = azimuthTo;

        float[] orientation = new float[3];
        float[] rMat = new float[9];
        SensorManager.getRotationMatrixFromVector(rMat, event.values);
        azimuthTo = (int) ( Math.toDegrees( SensorManager.getOrientation( rMat, orientation )[0] ) + 360 ) % 360;

        mAzimuthListener.onAzimuthChanged(azimuthFrom, azimuthTo);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
