package com.example.phoneapp.app.internet.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.phoneapp.app.internet.Config;
import com.example.phoneapp.app.internet.Internet;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pp on 5/15/14.
 */
public class AsyncImgDownload extends AsyncTask <String, Integer, Bitmap>{
    final static String __classLabel = "Internet:";
    Progress progress;
    void LOG(String m){ Log.d(this.__classLabel, m); }

    @Override
    protected Bitmap doInBackground(String... urls) {
        this.LOG("starting img download");
        this.progress = new Progress(new TimerTask() {
            @Override
            public void run() {
                publishProgress();
            }
        });
        this.progress.begin(500);

       // this.publishProgress();

        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        Bitmap b = null;
        this.publishProgress();
        try {
            response = httpclient.execute(new HttpGet(urls[0]));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                b = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.size());
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {/*  */}
        catch (IOException e) {/*  */}
        return b;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        Config.context.loadSome();
    }
    @Override
    protected void onPostExecute(Bitmap result) {
        this.progress.end();
        if (result != null){

            this.LOG("BITMAP DONE: " + result.getWidth() + "x" + result.getHeight());

            if (Config.context != null)
                Config.context.setImage(result);
            else
                this.LOG("config.context was null.");

        }else{
            this.LOG("BITMAP WAS NULL!");
        }

    }
    @Override
    protected void onCancelled(){
        super.onCancelled();
        this.progress.end();
    }

}
