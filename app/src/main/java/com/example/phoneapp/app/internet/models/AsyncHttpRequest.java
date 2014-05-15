package com.example.phoneapp.app.internet.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.phoneapp.app.R;
import com.example.phoneapp.app.internet.Config;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by pp on 5/15/14.
 */
public class AsyncHttpRequest extends AsyncTask <String,Integer, String>{
    final static String __classLabel = "Internet:";
    void LOG(String m){ Log.d(this.__classLabel, m); }
    Progress progress;

    @Override
    protected String doInBackground(String... urls) {
        this.LOG("starting");
        this.progress = new Progress(new TimerTask() {
            @Override
            public void run() {
                publishProgress();
            }
        });
        this.progress.begin(600);
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = "";
        try {
            response = httpclient.execute(new HttpGet(urls[0]));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                responseString = out.toString();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {/*  */}
        catch (IOException e) {/*  */}
        return responseString;
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        Config.context.loadSome();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        this.progress.end();
    }

    @Override
    protected void onPostExecute(String result){
        this.progress.end();
        if (Config.context != null) {
            Config.context.loadDone();
            Config.context.parseImgurResponse(result);
        }else{
            this.LOG("config.context was null.");
        }
    }
}
