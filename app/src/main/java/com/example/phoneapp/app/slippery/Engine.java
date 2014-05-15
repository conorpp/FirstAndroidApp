package com.example.phoneapp.app.slippery;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by pp on 5/12/14.
 */
public class Engine extends Thread {

    final static String __classLabel = "GameThread:";


    private SurfaceHolder sHolder;
    private slippery gPanel;
    private Canvas canvas;

    private int UPS = 30;
    private int FPS = 30;
    private int Fperiod = 1000 / this.FPS;
    private int Uperiod = 1000 / this.UPS;
    private int ticks = 0;

    private boolean running;


    public Engine(SurfaceHolder surfaceHolder, slippery gamePanel){
        super();
        this.sHolder = surfaceHolder;

        this.gPanel = gamePanel;


    }

    public int getFPS(){return this.FPS;}
    public int getPeriod(){return this.Fperiod;}
    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setFPS(int fps){
        this.FPS = fps;
        this.Fperiod = 1000 / fps;
    }

    @Override
    public void run() {
        this.LOG("Starting to run");

        long start, diff;
        long leftover;

        while (this.running) {
            this.canvas = null;
            int maxSkips = 5, skips;
            if (!this.running){
                this.LOG("this thread is over!");
            }
            try{
                canvas = this.sHolder.lockCanvas();

                synchronized (this.sHolder) {

                    skips = 0;
                    start = System.currentTimeMillis();
                    this.gPanel.updateGame(this.canvas);
                    this.gPanel.updateDisplay(this.canvas);
                    diff = System.currentTimeMillis() - start;
                    leftover =  this.Fperiod - diff;
                    if (leftover > 0) {
                        try {
                            Thread.sleep(leftover);
                        } catch (InterruptedException e) {/* */}
                    }
                    while (leftover < 0 && skips < maxSkips) {
                        this.gPanel.updateGame(this.canvas);
                        leftover += this.Uperiod;
                        skips++;
                    }
                }
            }finally {
                if (this.canvas != null){
                    this.sHolder.unlockCanvasAndPost(this.canvas);
                }
            }
            //this.ticks++;

            if (!this.running){
                this.LOG("this thread is over!");
            }
        }
        //this.LOG("game ran for "+this.ticks+" ticks");
    }



    void LOG(String m){ Log.d(this.__classLabel, m); }
}
