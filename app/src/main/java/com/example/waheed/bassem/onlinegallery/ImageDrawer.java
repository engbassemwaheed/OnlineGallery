package com.example.waheed.bassem.onlinegallery;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageDrawer implements LoaderManager.LoaderCallbacks<Bitmap> {
    private LoaderManager mLoaderManager;
    private Context mContext;
    private String mImageURL;
    private SparseArray<ImageView> mImageViewSparseArray = new SparseArray<>();
    private static SparseArray<Bitmap> mCachingArray = new SparseArray<>();

    public ImageDrawer() {
    }

    public void drawImage(Context context, LoaderManager loaderManager, String imageURL, ImageView imageView, int id) {
        //Log.e("Noha", "drawImage was called with id = " + id);
        mImageURL = imageURL;
        if (mCachingArray.get(id) != null) {
            Log.e("Noha", "caching works !! " + id);
            imageView.setImageBitmap(mCachingArray.get(id));
            return;
        }
        if (mImageViewSparseArray.get(id) == null) {
            mContext = context;
            mLoaderManager = loaderManager;
            Log.e("Noha", "not null this is a new data " + id);
            mImageViewSparseArray.append(id, imageView);
            mLoaderManager.initLoader(id, null, this).forceLoad();
        }
    }

    public void eraseCache () {
        mCachingArray = new SparseArray<>();
        mImageViewSparseArray = new SparseArray<>();
        Log.e("Noha", "Erase Cache!!!");
    }

    @Override
    public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
        //Log.e("Noha", "ImageGrabber was created with id = " + id);
        mImageViewSparseArray.get(id).setImageBitmap(null);
        return new ImageGrabber(mContext, mImageURL);
    }

    @Override
    public void onLoadFinished(Loader<Bitmap> loader, Bitmap data) {
        //Log.e("Noha", "finishe of id = " + loader.getId());
        ImageView imageView = mImageViewSparseArray.get(loader.getId());
        if (imageView != null) {
            //Log.e("Noha", "finished drawing adn  the Image with position = " + loader.getId());
            imageView.setImageBitmap(data);
            mImageViewSparseArray.remove(loader.getId());
            mCachingArray.append(loader.getId(), data);
        }
    }


    @Override
    public void onLoaderReset(Loader<Bitmap> loader) {
        Log.e("Noha", "loader reset " + loader.getId());
    }

}

