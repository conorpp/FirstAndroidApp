package com.example.phoneapp.app.timers;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.phoneapp.app.R;

/**
 * Created by pp on 5/11/14.
 */

public class timer extends Activity {
    static final String __classLabel = "InternetActivity: ";
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.LOG("onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internet);
        this.title = (TextView) findViewById(R.id.title);
    }

    public void startTimer(View v){
        this.LOG("touched");
        EditText e = (EditText) findViewById(R.id.editText);
        this.LOG("VAL: "+e.getText().toString());
        try {
            int num = Integer.parseInt(e.getText().toString());
            new asyncDownload().execute(num);
        }catch (NumberFormatException err){
            this.title.setText("NaN");
        }
    }

    protected void finished(){
        this.title.setText("The async task finished");
    }

    public class asyncDownload extends AsyncTask<Integer, Integer, Long> {
        final static String __classLabel = "Async";

        @Override
        protected Long doInBackground(Integer ... ints){
            this.LOG("GOT ints "+ints[0]);
            for(int i = ints[0]; i>=0; i--) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}
                this.publishProgress(i);
            }

            return ((long) ints[0]);
        }
        @Override
        protected void onProgressUpdate(Integer ... progress){
            super.onProgressUpdate(progress);
            title.setText("" + progress[0]);
            this.LOG("PROGRESS: "+progress[0]);
        }
        @Override
        protected void onPostExecute(Long result){
            super.onPostExecute(result);
            this.LOG("ALL DONE: "+result);
            ((TextView)findViewById(R.id.title)).setText("All done!");
        }
        void LOG(String m){Log.d(this.__classLabel, m);}
    }


    void LOG(String m){ Log.d(this.__classLabel, m); }
}
