package com.example.waheed.bassem.onlinegallery;

/**
 * Contains everything about an image
 */

public class ImageItem {

    private String mPreviewURL;
    private String mWebFormatURL;
    private String mTags;
    private String mPageURL;
    private String mUserName;
    private String mUserImageURL;
    private String mViews;
    private String mLikes;
    private String mFavs;
    private String mDownloads;

    public ImageItem(String previewURL,
                     String webFormatURL,
                     String tags,
                     String pageURL,
                     String userName,
                     String userImageURL,
                     String views,
                     String likes,
                     String favs,
                     String downloads) {
        mPreviewURL = previewURL;
        mWebFormatURL = webFormatURL;
        mTags = tags;
        mPageURL = pageURL;
        mUserName = userName;
        mUserImageURL = userImageURL;
        mViews = views;
        mLikes = likes;
        mFavs = favs;
        mDownloads = downloads;
    }

    public String getPreviewURL () { return mPreviewURL; }

    public String getWebFormatURL () { return mWebFormatURL; }

    public String getTags () { return mTags; }

    public String getPageURL () { return mPageURL; }

    public String getUserName () { return mUserName; }

    public String getUserImageURL () { return mUserImageURL; }

    public String getViews () { return mViews; }

    public String getLikes () { return mLikes; }

    public String getFavs () { return mFavs; }

    public String getDownloads () { return mDownloads; }

}
