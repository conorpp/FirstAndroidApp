package com.example.phoneapp.app.sensors;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.phoneapp.app.R;

import junit.framework.Test;

/**
 * Created by pp on 5/9/14.
 */
public class SensorPicker extends Activity implements SensorEventListener, AdapterView.OnItemSelectedListener {
    static final String __classLabel = "SensorPicker: ";

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private TextView textViews[];
    private TextView title;
    private Spinner options;


    private int SensorType = -1;
    private int supportedSensorTypes[] = {Sensor.TYPE_AMBIENT_TEMPERATURE, Sensor.TYPE_GYROSCOPE, Sensor.TYPE_PRESSURE,
                                          Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_LIGHT, Sensor.TYPE_MAGNETIC_FIELD,
                                          Sensor.TYPE_PROXIMITY, Sensor.TYPE_PROXIMITY, Sensor.TYPE_RELATIVE_HUMIDITY,
                                          Sensor.TYPE_ROTATION_VECTOR};
    private String units[] = new String[3];
    private String prefixes[] = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensorpicker);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        this.options = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sensorOptions,
                                                                            android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.options.setAdapter(adapter);
        this.options.setOnItemSelectedListener(this);

        this.textViews = new TextView[]{
                (TextView) findViewById(R.id.X),
                (TextView) findViewById(R.id.Y),
                (TextView) findViewById(R.id.Z)
        };

        this.title = (TextView) findViewById(R.id.title);
        if (this.SensorType == -1){
            this.beginAccelerometer();
        }
    }

    @Override
    protected void onResume(){          // Save on computation
        super.onResume();
        this.LOG("onResume");
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause(){           // Save on computation
        super.onPause();
        this.LOG("onPause");
        mSensorManager.unregisterListener(this);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        this.LOG("THE ACCURACY CHANGED!!");
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == this.SensorType) {
            for (int i=0; i<3; i++){
                if (this.units[i] != "")
                    this.textViews[i].setText(this.prefixes[i] + " " + event.values[i] + " " + this.units[i]);
            }

        }
    }

    private void setSensor(int sensortype){

        mSensorManager.unregisterListener(this);
        for (int i=0; i<3; i++)
            this.textViews[i].setText("");

        /* Return if sensor type is not supported */
        if (! this.supported(sensortype)){
            this.LOG("That Sensor is not supported. ");
            this.title.setText("That Sensor is not supported on your device. ");
            return;
        }else{
            this.title.setText("Pick a sensor");
        }
        this.SensorType = sensortype;


        mSensor = mSensorManager.getDefaultSensor(this.SensorType);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private boolean supported(int sensorType){
        for (int sType : this.supportedSensorTypes){
                if (sType == sensorType && (mSensorManager.getDefaultSensor(sType) != null))
                    return true;
        }
        return false;
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        this.LOG("Selected: "+pos);
        switch (pos){
            case 0:
                this.beginAccelerometer();
            break;
            case 1:
                this.beginGyroMeter();
            break;
            case 2:
                this.beginTemperatureMeter();
            break;
            case 3:
                this.beginLightMeter();
            break;
            case 4:
                this.beginPressureMeter();
            break;
            case 5:
                this.beginMagnoMeter();
            break;
            case 6:
                this.beginProxyMeter();
            break;
            case 7:
                this.beginHumidMeter();
            break;
            case 8:
                this.beginRotationMeter();
            break;
        }



    }

    public void onNothingSelected(AdapterView<?> parent) {
        this.LOG("NOTHING SELECTED");
    }

    public void beginAccelerometer(){
        this.LOG("Changing sensor to accelerometer");

        this.setUnits("m/s^2","m/s^2","m/s^2");
        this.setPrefix("X","Y","Z");

        this.setSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void beginMagnoMeter(){
        this.LOG("Changing sensor to magnometer");

        this.setUnits("uT","uT","uT");
        this.setPrefix("X","Y","Z");

        this.setSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }
    public void beginLightMeter(){
        this.LOG("Changing sensor to lightMeter");

        this.setUnits("lux","","");

        this.setSensor(Sensor.TYPE_LIGHT);
    }
    public void beginPressureMeter(){
        this.LOG("Changing sensor to PressureMeter");

        this.setUnits("hPa","","");

        this.setSensor(Sensor.TYPE_PRESSURE);
    }

    public void beginTemperatureMeter(){
        this.LOG("Changing sensor to TemperatureMeter");

        this.setUnits("C","","");

        this.setSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
    }
    public void beginGyroMeter(){
        this.LOG("Changing sensor to GyroMeter");

        this.setUnits("rad/s","rad/s","rad/s");
        this.setPrefix("X","Y","Z");

        this.setSensor(Sensor.TYPE_GYROSCOPE);
    }
    public void beginProxyMeter(){
        this.LOG("Changing sensor to ProxyMeter");

        this.setUnits("cm","","");

        this.setSensor(Sensor.TYPE_PROXIMITY);
    }
    public void beginHumidMeter(){
        this.LOG("Changing sensor to HumidityMeter");

        this.setUnits("%","","");

        this.setSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
    }
    public void beginRotationMeter(){
        this.LOG("Changing sensor to HumidityMeter");

        this.setUnits(" "," "," ");

        this.setSensor(Sensor.TYPE_ROTATION_VECTOR);
    }
    private void setUnits(String ... _units){
        this.clearPrefix();
        for (int i=0; i<3; i++)
            this.units[i] = _units[i];
    }
    private void setPrefix(String ... _units){
        for (int i=0; i<3; i++)
            this.prefixes[i] = _units[i];
    }
    private void clearPrefix(){
        for (int i=0; i<3; i++)
            this.prefixes[i] = "";
    }
    void LOG(String m){
        Log.d(this.__classLabel, m);
    }


}
