package com.example.waheed.bassem.onlinegallery;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<ImageItem>>,
        AdapterView.OnItemClickListener, TextView.OnEditorActionListener {

    private GridView mGridView;
    private ArrayList<ImageItem> mImageItemArrayList;
    private GridAdapter mGridAdapter;
    private static String mSearchKeyword = new String();
    private final int SEARCH_EXECUTOR_ID = 1000;
    private boolean mAreItemsLoaded = false;
    private EditText mSearchEditText;
    private ProgressBar mProgressBar;
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("Noha", "on create ");

        mGridView = (GridView) findViewById(R.id.image_grid_view);
        mSearchEditText = (EditText) findViewById(R.id.search_edit_text);
        mImageItemArrayList = new ArrayList<>();
        mGridAdapter = new GridAdapter(this, mImageItemArrayList, getSupportLoaderManager());
        mTextView = (TextView) findViewById(R.id.grid_empty_text_view);
        mProgressBar = (ProgressBar) findViewById(R.id.grid_empty_progress_bar);
        mProgressBar.setVisibility(View.GONE);

        mGridView.setAdapter(mGridAdapter);
        mTextView.setText(getString(R.string.search_for_an_image));

        mGridView.setOnItemClickListener(this);
        mSearchEditText.setOnEditorActionListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Noha", "ON Satrt");
        if (!mSearchKeyword.isEmpty() && mSearchKeyword != null) {
            Log.e("Noha", "On STart- creating calling the search loader ");
            getSupportLoaderManager().initLoader(SEARCH_EXECUTOR_ID, null, this).forceLoad();
        }
    }

    @Override
    public Loader<ArrayList<ImageItem>> onCreateLoader(int id, Bundle args) {
        Log.e("Noha", "Searching loader is created     " + mSearchKeyword);
        mAreItemsLoaded = false;
        mTextView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        return new SearchExecutor(this, mSearchKeyword);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ImageItem>> loader, ArrayList<ImageItem> data) {
        Log.e("Noha", "on load finish     " + mSearchKeyword);
        SearchExecutor searchExecutor = (SearchExecutor) loader;
        if(searchExecutor.hasNewData()){
            Log.e("Noha", "NEW DATAAAAAAAAAAAAAAAAAAAAAAAA");
            mGridAdapter.clearCachedData();
        }
        if (data != null) {
            Log.e("Noha", "Searching done with number of items = " + data.size());
            mImageItemArrayList = data;
            mGridAdapter.updateImageList(mImageItemArrayList);
            mProgressBar.setVisibility(View.GONE);
            mAreItemsLoaded = true;
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ImageItem>> loader) {
        Log.e("Noha", "Loader reset BHAHAHAHAhahaah");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mAreItemsLoaded) {
            ImageItem imageItem = mImageItemArrayList.get(position);
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(getString(R.string.webformat_key), imageItem.getWebFormatURL());
            intent.putExtra(getString(R.string.views_key), imageItem.getViews());
            intent.putExtra(getString(R.string.likes_key), imageItem.getLikes());
            intent.putExtra(getString(R.string.tags_key), imageItem.getTags());
            intent.putExtra(getString(R.string.page_url_key), imageItem.getPageURL());
            intent.putExtra(getString(R.string.favs_key), imageItem.getFavs());
            intent.putExtra(getString(R.string.downloads_key), imageItem.getDownloads());
            intent.putExtra(getString(R.string.user_name_key), imageItem.getUserName());
            intent.putExtra(getString(R.string.id_key), position);

            startActivity(intent);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        Log.e("Noha", "action id = " + actionId);
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            Log.e("Noha", "name entered and force loading");
            mSearchKeyword = mSearchEditText.getText().toString();
            getSupportLoaderManager().destroyLoader(SEARCH_EXECUTOR_ID);
            getSupportLoaderManager().initLoader(SEARCH_EXECUTOR_ID, null, this).forceLoad();

        }
        return false;
    }
}
