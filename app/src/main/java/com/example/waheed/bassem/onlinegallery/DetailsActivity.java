package com.example.waheed.bassem.onlinegallery;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    private ImageView mSelectedImageView;
    private ImageButton mProvidedByImageButton;
    private TextView mViews;
    private TextView mLikes;
    private TextView mTags;
    private TextView mFavs;
    private TextView mDownloads;
    private TextView mUserName;
    private String mWebFormatURL;
    private String mPageURL;
    private ImageDrawer mImageDrawer;
    private final int DEFAULT_ID = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initViews();
        getDataFromIntent();
        initListeners();

    }

    private void initListeners() {
        mProvidedByImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mPageURL));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.save_image) {
            Toast.makeText(this, "Save is clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDataFromIntent () {
        Intent intent = getIntent();
        if (intent != null) {
            mFavs.setText(intent.getStringExtra(getString(R.string.favs_key)));
            mViews.setText(intent.getStringExtra(getString(R.string.views_key)));
            mLikes.setText(intent.getStringExtra(getString(R.string.likes_key)));
            mTags.setText(intent.getStringExtra(getString(R.string.tags_key)));
            mUserName.setText(intent.getStringExtra(getString(R.string.user_name_key)));
            mDownloads.setText(intent.getStringExtra(getString(R.string.downloads_key)));
            mPageURL = intent.getStringExtra(getString(R.string.page_url_key));
            mWebFormatURL = intent.getStringExtra(getString(R.string.webformat_key));
            int id = intent.getIntExtra(getString(R.string.id_key), DEFAULT_ID);
            Log.e("Noha", mWebFormatURL);
            mImageDrawer.drawImage(this, getSupportLoaderManager(), mWebFormatURL, mSelectedImageView, id);
        }
    }

    private void initViews () {
        mImageDrawer = new ImageDrawer();
        mSelectedImageView = (ImageView) findViewById(R.id.image_web_format);
        mViews = (TextView) findViewById(R.id.views_text_view);
        mDownloads = (TextView) findViewById(R.id.downloads_text_view);
        mTags = (TextView) findViewById(R.id.tags_text_view);
        mLikes = (TextView) findViewById(R.id.likes_text_view);
        mUserName = (TextView) findViewById(R.id.user_name_text_view);
        mFavs = (TextView) findViewById(R.id.favs_text_view);
        mProvidedByImageButton = (ImageButton) findViewById(R.id.provided_by_image_button);
    }
}
