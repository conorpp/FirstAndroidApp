package com.example.phoneapp.app.internet.models;

import android.content.Context;

import com.example.phoneapp.app.internet.Config;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pp on 5/15/14.
 */
public class Progress {

    public Progress(TimerTask t){
        this.timeTask = t;
    }
    static TimerTask timeTask;
    static Timer timer = null;

    static public  void begin(int millis){
        if (timer != null)
            end();
        timer = new Timer();
        timer.scheduleAtFixedRate(timeTask, 0, millis);
    }

    static public void end(){
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }

}
