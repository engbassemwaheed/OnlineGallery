package com.example.waheed.bassem.onlinegallery;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;



public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ImageItem> mImageItemArrayList;
    private ImageDrawer mImageDrawer;
    private LoaderManager mLoaderManager;

    public GridAdapter (Context context, ArrayList<ImageItem> imageItemArrayList, LoaderManager loaderManager) {
        mContext = context;
        mImageItemArrayList = imageItemArrayList;
        mImageDrawer = new ImageDrawer();
        mLoaderManager = loaderManager;
    }

    @Override
    public int getCount() {
        return mImageItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
        }

      //  Log.e("Noha", "                                        getView called and the position = " + (position));
        ImageItem imageItem = mImageItemArrayList.get(position);
        String imageURL = imageItem.getWebFormatURL();
        ImageView imageView = (ImageView) convertView.findViewById(R.id.grid_item_image);
        TextView textView = (TextView) convertView.findViewById(R.id.grid_item_text);
        mImageDrawer.drawImage(mContext, mLoaderManager, imageURL, imageView, position);

        return convertView;
    }

    public void updateImageList (ArrayList<ImageItem> imageItemArrayList) {
        mImageItemArrayList = imageItemArrayList;
        notifyDataSetChanged();
    }

    public void clearCachedData() {
        mImageDrawer.eraseCache();
        mImageItemArrayList = new ArrayList<>();
        notifyDataSetInvalidated();
    }
}
