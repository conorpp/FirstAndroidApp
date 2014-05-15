package com.example.phoneapp.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.phoneapp.app.slippery.slipperyActivity;
import com.example.phoneapp.app.countdown.timer;
import com.example.phoneapp.app.sensors.SensorPicker;


public class MainActivity extends Activity {
    static final String __classLabel = "MainActivity: ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart(){
        super.onStart();
        this.LOG("onStart");
    }
    @Override
    protected void onResume(){
        super.onResume();
        this.LOG("onResume");
    }
    @Override
    protected void onPause(){
        super.onPause();
        this.LOG("onPause");
    }
    @Override
    protected void onStop(){
        super.onStop();
        this.LOG("onStop");
    }

    public void beginSensors(View view){
        this.LOG("the sensors has been touched!");
        Intent intent = new Intent(this, SensorPicker.class);
        startActivity(intent);
    }
    public void beginCountdown(View view){
        this.LOG("the timer has been touched!");
        Intent intent = new Intent(this, timer.class);
        startActivity(intent);
    }
    public void beginSlippery(View view){
        this.LOG("the slippery has been touched!");
        Intent intent = new Intent(this, slipperyActivity.class);
        startActivity(intent);
    }
    void LOG(String m){
        Log.d(this.__classLabel , m);
    }

}
