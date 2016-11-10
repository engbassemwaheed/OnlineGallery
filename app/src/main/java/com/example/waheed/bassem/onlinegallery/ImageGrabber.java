package com.example.waheed.bassem.onlinegallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by basse on 11/7/2016.
 */

public class ImageGrabber extends AsyncTaskLoader<Bitmap> {

    private final String REQUEST_METHOD = "GET";
    private String mImageURL;

    public ImageGrabber(Context context, String imageURL) {
        super(context);
        mImageURL = imageURL;
    }


    @Override
    public Bitmap loadInBackground() {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        Bitmap bitmap = null;
        URL url = null;
        try {
            url = new URL(mImageURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (url == null) {
            return null;
        }

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.connect();

            inputStream = connection.getInputStream();
            if (inputStream == null) {
                return null;
            }

            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // disconnecting the connection
            // and make sure that it was opened before (isn't null)
            if (connection != null) {
                connection.disconnect();
            }
            // closing the inputStream
            // and make sure that it was opened before (isn't null)
            // and handle the exception if it wasn't closed
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // handling the exception and returning null
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    public void setImageURL(String url) {
        mImageURL = url;
    }
}
