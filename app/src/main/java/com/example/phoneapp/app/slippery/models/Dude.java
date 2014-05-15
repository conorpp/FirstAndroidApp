package com.example.phoneapp.app.slippery.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.example.phoneapp.app.slippery.Config;
import com.example.phoneapp.app.slippery.models.Accelerometer.AccelData;
/**
 * Created by pp on 5/13/14.
 */
public class Dude {
    final static String __classLabel = "Character:";
    void LOG(String m){ Log.d(this.__classLabel, m); }

    Bitmap bitmap;
    int width = 40, height = 40;
    int x = 40, y = 40;
    int dxDirection = 1, dyDirection =1;
    public float dxMagnitude = 2, dyMagnitude = 2;
    float ay = 0, ax = 0;
    boolean Xcollision;
    boolean Ycollision;


    public Dude(Bitmap b){
        this.bitmap = b;

    }
    public void draw(Canvas c){
        c.drawBitmap(bitmap, this.x, this.y, null);
    }

    public void wander(Canvas c){
        this.checkEdgeDirection(c);
        this.y += (this.dyDirection);
        this.x += (this.dxDirection);
    }
    /* Reverse direction if there's a collision */
    private void checkEdgeDirection(Canvas c){
        if (this.y >= c.getHeight())
            this.dyDirection = -1;
        else if ((this.y - this.height) <= 0)
            this.dyDirection = 1;

        if ((this.x + this.width) >= c.getWidth())
            this.dxDirection = -1;
        else if (this.x <= 0)
            this.dxDirection = 1;
    }
    /* Reverse velocity direction if there's a collision */
    private boolean checkEdgeMagnitude(Canvas c){
        if (this.y + this.height >= c.getHeight())
            setYDirection(-1);
        else if ((this.y) <= 0)
            setYDirection( 1);

        if ((this.x + this.width) >= c.getWidth())
            setXDirection( -1);
        else if (this.x <= 0)
            setXDirection( 1);

        boolean col = this.Xcollision || this.Ycollision;
        this.Xcollision = this.Ycollision = false;
        return col;
    }
    /* Collide off of other dudes */
    private void checkOtherDudes(){
        int myLeft = this.x,
            myRight = this.x+this.width,
            myTop = this.y + this.height,
            myBottom = this.y;
        for(int i= Config.slipperyContext.dudes.size()-1; i>=0; --i){
            Dude other = Config.slipperyContext.dudes.get(i);
            if (Config.slipperyContext.dudes.get(i) == this) continue;
            int otherLeft = other.x,
                otherRight = other.x + other.width,
                otherTop = other.y + other.height,
                otherBottom = other.y;

            // Both in same Y
            boolean sameY = (myBottom > otherBottom && myBottom < otherTop) ||
                            (myTop > otherBottom && myTop < otherTop);

            if (sameY){
                int halfX = other.width/2;
                /* bounce off my right */
                if (myRight >= otherLeft && myRight < otherLeft + halfX){
                    this.setXDirection(-1);
                    other.setXDirection(1);
                }else
                /* bounce off my left */
                if (myLeft <= otherRight && myLeft > otherRight - halfX){
                    this.setXDirection(1);
                    other.setXDirection(-1);
                }
            }
            boolean sameX = (myLeft > otherLeft && myLeft < otherRight) ||
                            (myRight > otherLeft && myRight < otherRight);
            if (sameX){
                int halfY = other.width/2;
                /* bounce off my top */
                if (myTop >= otherBottom && myTop < otherBottom + halfY ){
                    this.setYDirection(-1);
                    other.setYDirection(1);
                }else
                /* bounce off my bottom */
                if(myBottom <= otherTop && myBottom > otherTop - halfY){
                    this.setYDirection(1);
                    other.setYDirection(-1);
                }
            }

        }
    }
    private float abs(float val){ return (val < 0 ? val*-1 : val); }
    private void setXDirection(int sign){
        this.Xcollision = true;
        this.dxMagnitude = (this.dxMagnitude < 0 ? this.dxMagnitude*-1 : this.dxMagnitude)*sign ;
    }
    private void setYDirection(int sign){
        this.Ycollision = true;
        this.dyMagnitude = (this.dyMagnitude < 0 ? this.dyMagnitude*-1 : this.dyMagnitude)*sign ;
    }
    public void setAcceleration(AccelData data){
        this.ax = data.float_data[1];
        this.ay = data.float_data[0];
    }
    void updateVelocity(){
        this.dxMagnitude += (this.ax*2);
        this.dyMagnitude += (this.ay*2);
    }
    void addFriction(){
        if (this.dyMagnitude!=0) this.dyMagnitude = (this.dyMagnitude*8)/9;
        if (this.dxMagnitude!=0) this.dxMagnitude = (this.dxMagnitude*8)/9;
    }
    void updatePosition(){
        this.y += (this.dyMagnitude);
        this.x += (this.dxMagnitude);
    }
    public void moveFromGravity(Canvas c){
        this.updateVelocity();
        this.checkEdgeMagnitude(c);
        this.checkOtherDudes();
        this.addFriction();
        this.updatePosition();
    }

}
