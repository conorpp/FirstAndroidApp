package com.example.phoneapp.app.internet;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phoneapp.app.R;
import com.example.phoneapp.app.internet.models.AsyncHttpRequest;
import com.example.phoneapp.app.internet.models.AsyncImgDownload;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pp on 5/15/14.
 */
public class Internet extends Activity {
    final static String __classLabel = "Internet: ";
    private void LOG(String m){ Log.d(this.__classLabel, m); }

    List <String> imgLinks;
    private int index = 0;
    private boolean initializing = true;
    private AsyncHttpRequest refreshRequest = null;
    TextView httpResponse;
    TextView title;
    TextView loader;
    ImageView img;

    AsyncImgDownload imgDownload = null;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);

        setContentView(R.layout.internet);
        this.httpResponse = (TextView) findViewById(R.id.httpResponse);
        this.title = (TextView) findViewById(R.id.title);
        this.loader = (TextView) findViewById(R.id.loader);
        this.img = (ImageView)findViewById(R.id.imageView);
        this.httpResponse.setText("");
        this.loader.setText("");
        Config.context = this;
        this.refreshImgLinks(this.title);
    }


    @Override
    protected void onResume(){
        super.onResume();
    }

    // Turns imgur homepage HTML into an array of img links
    public void parseImgurResponse(String res){
        Pattern findLinks = Pattern.compile("(i.imgur.com)(.+?)(jpg)");
        Matcher m = findLinks.matcher(res);
        while(m.find()){
            String url = m.group();
            int ib = url.indexOf("b.jpg");
            if (ib != -1){      // Remove the b to link to high res image.
                url = url.substring(0,ib) + ".jpg";
            }
            url = "http://" + url;
            this.imgLinks.add(url);
        }
        this.initializing = false;
        this.index = 0;
        this.nextImg(this.title);

    }
    private void getAnotherImg(int i){
        if (this.imgDownload == null){
            this.index += i;

            if (this.index < 1)
                this.index = this.imgLinks.size();
            else if(this.index > this.imgLinks.size())
                this.index = 1;
            String url = this.imgLinks.get(this.index-1);

            this.imgDownload = new AsyncImgDownload();
            this.imgDownload.execute(url);
            this.title.setText(this.index+"/"+this.imgLinks.size()+" Pics");
        }else{
            if (this.imgDownload.isCancelled() || this.imgDownload.getStatus() == AsyncTask.Status.FINISHED){
                this.imgDownload = null;
            }else
                this.imgDownload.cancel(true);
            this.getAnotherImg(i);
        }
    }

    public void nextImg(View view){
        if (initializing) return;
        this.getAnotherImg(1);
    }
    public void previousImg(View view){
        if (initializing) return;
        this.getAnotherImg(-1);
    }

    public void refreshImgLinks(View view){
        if (this.refreshRequest == null) {
            this.refreshRequest = new AsyncHttpRequest();
            this.refreshRequest.execute("http://imgur.com/");
            this.imgLinks = new ArrayList<String>();
        }else if (! this.initializing){
            /* Recurse until ready to start another async task */
            if (this.refreshRequest.isCancelled() || this.refreshRequest.getStatus() == AsyncTask.Status.FINISHED)
                this.refreshRequest = null;
            else
                this.refreshRequest.cancel(true);
            this.refreshImgLinks(this.title);
        }

    }

    public void setImage(Bitmap b){
        this.loadDone();
        this.img.setImageBitmap(b);
    }
    /*
    * Animate the load icon
    * */
    public void loadSome(){
        this.loader.setText(this.loader.getText()+".");
        if (this.loader.getText().toString().indexOf("........") != -1)
            this.loadDone();
    }
    public void loadDone(){
        this.loader.setText("");
    }

}
