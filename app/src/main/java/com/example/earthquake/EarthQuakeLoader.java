package com.example.earthquake;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.IOException;
import java.util.List;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class EarthQuakeLoader extends AsyncTaskLoader<List<earthquake>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = EarthQuakeLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link //EarthquakeLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public EarthQuakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<earthquake> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<earthquake> earthquakes = null;
        try {
            earthquakes = QueryUtils.extractEarthquakes(mUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return earthquakes;
    }
}