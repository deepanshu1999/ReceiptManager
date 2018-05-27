package com.example.deepanshuaggarwal.receiptvault;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Deepanshu Aggarwal on 03-04-2018.
 */

public class DownloadImage extends AsyncTask<String,Void,Bitmap>{
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap myBitmap=null;
                URL url;
                HttpURLConnection urlConnection=null;
                try {
                    url=new URL(strings[0]);
                    urlConnection=(HttpURLConnection)url.openConnection();
                    InputStream in=urlConnection.getInputStream();
                    myBitmap= BitmapFactory.decodeStream(in);

                }
                catch (Exception e){e.printStackTrace();}
                return myBitmap;
            }

        }
