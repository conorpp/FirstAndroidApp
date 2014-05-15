package com.example.phoneapp.app.slippery;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.phoneapp.app.R;

/**
 * Created by pp on 5/12/14.
 */
public class slipperyActivity extends Activity {
    final static String __classLabel = "GameActivity:";
    void LOG(String m){ Log.d(this.__classLabel, m); }
    slippery Game;

    @Override
    public void onCreate(Bundle savedState){
        super.onCreate(savedState);

        // turn off title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // make full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.Game = new slippery(this);
        setContentView(this.Game);

        this.LOG("view added.");
    }

    @Override
    protected void onDestroy() {
        this.LOG( "Destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        this.LOG("Stopping...");
        super.onStop();
    }


}
