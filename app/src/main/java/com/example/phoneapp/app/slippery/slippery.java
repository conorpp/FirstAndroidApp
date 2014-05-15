package com.example.phoneapp.app.slippery;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.phoneapp.app.R;
import com.example.phoneapp.app.slippery.models.*;
import com.example.phoneapp.app.slippery.models.Dude;
import com.example.phoneapp.app.slippery.models.Accelerometer.AccelData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pp on 5/12/14.
 */
public class slippery extends SurfaceView implements  SurfaceHolder.Callback {
    final static String __classLabel = "Slippery:";
    void LOG(String m){ Log.d(this.__classLabel, m); }
    public Engine engine;

    public List<Dude> dudes;
    private Accelerometer accelerometer;
    private Bitmap dudeBitmap;

    private Paint slipperyText;
    private Paint buttonText;
    private Paint ButtonPaint;
    private Rect newDudeButton;
    private Canvas canvas = null;

    private long begin = System.currentTimeMillis();


    // void setFPS(int f){ this.fps = f; }

    public slippery(Context context) {
        super(context);
        this.engine = new Engine(getHolder(), this);
        Config.context = context;
        Config.slipperyContext = this;
        this.dudes = new ArrayList<Dude>();

        // adding the callback (this) to the surface holder to intercept events
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        // make the GamePanel focusable so it can handle events
        setFocusable(true);
        this.addGraphics();

        this.addDude();
        this.LOG("Constructor");
    }

    public void stop(){
        if (this.engine != null){
         //   this.engine.setRunning(false);
        }
    }

    private void addGraphics(){
        this.slipperyText = new Paint();
        this.slipperyText.setARGB(255, 255, 255, 255);
        this.slipperyText.setTextSize(55f);
        this.buttonText = new Paint();
        this.buttonText.setColor(Color.BLACK);
        this.buttonText.setTextSize(35f);
        this.ButtonPaint = new Paint();
        this.ButtonPaint.setColor(Color.CYAN);

        this.dudeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.favicon);
    }
    private void addGraphics(Canvas c){
        int top = 210;
        int bottom = top +100;
        int left = c.getWidth() - 280;
        int right = left + 200;
        this.newDudeButton = new Rect(left,top,right,bottom);
    }

    private void addDude(){
        this.dudes.add(new Dude(this.dudeBitmap));
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.LOG("SurfaceChanged");

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.LOG("Surface created");

        Canvas c = holder.lockCanvas();
        this.addGraphics(c);
        holder.unlockCanvasAndPost(c);

        this.accelerometer = new Accelerometer();
        this.accelerometer.start();
        engine.setRunning(true);
        this.begin = System.currentTimeMillis();
        engine.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.LOG("Surface has been destroyed . . . ");
        boolean retry = true;
        this.engine.setRunning(false);
        this.accelerometer.end();

        while (retry) {
            try {
                this.LOG("about to join thread ...");
                this.engine.join();
                this.LOG("thread has been joined");
                retry = false;
            }catch (InterruptedException e){
                this.LOG("thread threw exception");
            }
        }
        this.LOG("Surface has been destroyed safely . . . ");
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            this.LOG("Coords: x=" + event.getX() + ",y=" + event.getY());
            if ( this.buttonIsPressed(event.getX(), event.getY(), this.newDudeButton) ){
                this.LOG("BUTTON WAAS PRESSED!");
                this.addDude();
            }
        }
        return super.onTouchEvent(event);
    }
    public void onAccelerometer(AccelData data){
        for(int i = this.dudes.size()-1; i>=0; --i)
            this.dudes.get(i).setAcceleration(data);

    }
    @Override
    protected void onDraw(Canvas canvas) {
      //  this.LOG("onDraw");
    }

    protected void updateDisplay(Canvas c){
        if (c != null){
            this.drawBackground(c);
            this.displayFPS(c);
            for(int i = this.dudes.size() - 1; i>=0; --i)
                this.dudes.get(i).draw(c);
        }




    }
    protected void updateGame(Canvas c){
        if (c != null) {
            for (int i = this.dudes.size() - 1; i >= 0; --i)
                this.dudes.get(i).moveFromGravity(c);
        }

    }
    void displayFPS(Canvas c){
        /* time since last frame */

        int dif = ((int)System.currentTimeMillis()-(int)this.begin)+1;
        String msg = "FPS: "+(this.engine.getFPS()*this.engine.getPeriod())/dif;
        this.begin = System.currentTimeMillis();
        c.drawText(msg, c.getWidth()-280, 120, this.slipperyText);

    }

    void drawBackground(Canvas c){
        c.drawColor(Color.BLACK);
        //left, top, right, bottom
        c.drawRect(this.newDudeButton, this.ButtonPaint);
        c.drawText("Add a dude",this.newDudeButton.left,this.newDudeButton.bottom-20, this.buttonText);
    }
    private boolean buttonIsPressed(float x, float y, Rect button){

        return (y>button.top && y<button.bottom
           && x>button.left && x <button.right);

    }


}
