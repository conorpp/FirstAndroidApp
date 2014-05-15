package com.example.phoneapp.app.slippery.models;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.phoneapp.app.sensors.SensorPicker;
import com.example.phoneapp.app.slippery.Config;

/**
 * Created by pp on 5/14/14.
 */
public class Accelerometer implements SensorEventListener {

    SensorManager mSensorManager;
    Sensor aSensor;

    public Accelerometer(){
        mSensorManager = (SensorManager) Config.context.getSystemService(Context.SENSOR_SERVICE);
        this.aSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }
    public void start(){
        mSensorManager.registerListener(this, this.aSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    public void end(){
        mSensorManager.unregisterListener(this);
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Config.slipperyContext.onAccelerometer(new AccelData(event.values));
        }
    }
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public class AccelData{
        public int int_data[];
        public float float_data[];
        AccelData(float data[]){
            this.float_data = data;
            this.int_data = new int[3];
            for(int i = 0; i<3; i++)
                this.int_data[i] = (int)data[i];
        }
    }
}


