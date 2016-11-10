package com.example.waheed.bassem.onlinegallery;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * handel:
 * getting image and data from the web
 */

public class SearchExecutor extends AsyncTaskLoader<ArrayList<ImageItem>> {

    private String mSearchKeyWord;
    // &q=yellow+flowers
    private final String BASE_URL = "https://pixabay.com/api/?key=3694743-0779697d8fdf9cf949d9bac2d";
    private final String IMAGE_TYPE_PARAM = "image_type";
    private final String IMAGE_TYPE = "photo";
    private final String QUERY = "q";
    private final String REQUEST_METHOD = "GET";
    private static String mPreviousKeyWord = " ";
    private boolean mUsesCacheFlag = false;
    private static ArrayList<ImageItem> mResult;

    public SearchExecutor(Context context, String searchKeyWord) {
        super(context);

        Log.e("Noha", "New search = " + searchKeyWord + " Old search " +mPreviousKeyWord);

        if (searchKeyWord.equals(mPreviousKeyWord)) {
            mUsesCacheFlag = true;
            Log.e("Noha", "The same search word");
        }
        else {
            Log.e("Noha", "new search word");
            mUsesCacheFlag = false;
            mSearchKeyWord = modifySearchKeyWord(searchKeyWord);
        }
        mPreviousKeyWord = searchKeyWord;
        Log.e("Noha", "New search = " + mSearchKeyWord + " Old search " +mPreviousKeyWord);
    }

    @Override
    public ArrayList<ImageItem> loadInBackground() {
        Log.e("Noha", "Load in background");
        if (!mUsesCacheFlag) {

            URL url = getURL();
            if (url == null) {
                return null;
            }
            Log.e("Noha", "Got the new URL");
            String jsonString = getJsonString(url);
            if (jsonString == null) {
                return null;
            }
            mResult = parseJson(jsonString);

            Log.e("Noha", "done parsing !!");

            return mResult;
        } else {
            Log.e("Noha", "returning the previous result");
            return mResult;
        }
    }


    /**
     * adjusting the keyWord to match the URL format
     * removing the leading and trailing white spaces
     * replacing the white spaces between words with a plus sign
     * @param keyWord = the raw keyword
     * @return modified keyword
     */
    private String modifySearchKeyWord (String keyWord) {
        return keyWord.trim().replace(" ", "+");
    }

    private URL getURL () {
        Uri url = Uri.parse(BASE_URL);

        Uri.Builder builder = url.buildUpon();
        builder.appendQueryParameter(QUERY, mSearchKeyWord);
        builder.appendQueryParameter(IMAGE_TYPE_PARAM, IMAGE_TYPE);


        URL finalUrl = null;
        try {
            finalUrl = new URL(builder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return finalUrl;
    }

    private String getJsonString (URL url) {

        String jsonString = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        String line;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(REQUEST_METHOD);
            connection.connect();

            inputStream = connection.getInputStream();
            if (inputStream == null) {
                return null;
            }

            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);

            line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }

            if (stringBuilder.length() == 0) {
                return null;
            }

            jsonString = stringBuilder.toString();

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



        return jsonString;
    }

    private ArrayList<ImageItem> parseJson (String jsonString) {
        ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
        if (jsonString == null) {
            return null;
        }

        try {
            JSONObject root = new JSONObject(jsonString);
            JSONArray hits = root.getJSONArray("hits");

            for(int i = 0; i<hits.length(); i ++) {
                JSONObject item = hits.getJSONObject(i);
                String previewURL = item.optString("previewURL");
                String webFormatURL = item.optString("webformatURL");
                String tags = item.optString("tags");
                String userName = item.optString("user");
                String userNameImageURL = item.optString("userImageURL");
                String views = item.optString("views");
                String likes = item.optString("likes");
                String favs = item.optString("favorites");
                String downloads = item.optString("downloads");
                String pageURL = item.optString("pageURL");
                imageItems.add(new ImageItem(previewURL, webFormatURL, tags, pageURL, userName,
                        userNameImageURL, views, likes, favs, downloads));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return imageItems;
    }


    public boolean hasNewData() {
        return !mUsesCacheFlag;
    }
}
