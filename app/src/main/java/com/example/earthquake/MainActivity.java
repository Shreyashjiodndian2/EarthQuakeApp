package com.example.earthquake;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<earthquake>> {
    final String urls = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    public static final String LOG_TAG = MainActivity.class.getName();
    ListView earthquakeListView;
    private EarthQuakeAdapter adapter;
    ArrayList<earthquake> data;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_setting) {
            Intent intent = new Intent(this, Setting.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        adapter = new EarthQuakeAdapter(this, new ArrayList<earthquake>());
        earthquakeListView = findViewById(R.id.list);
        if (cm.getActiveNetworkInfo() != null) {
            if (cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected() && cm.getActiveNetworkInfo() != null) {
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.initLoader(1, null, this).forceLoad();
                earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        // Find the current earthquake that was clicked on
                        earthquake currentEarthquake = adapter.getItem(position);
                        // Convert the String URL into a URI object (to pass into the Intent constructor)
                        Uri earthquakeUri = Uri.parse(currentEarthquake.getmUrl());
                        // Create a new intent to view the earthquake URI
                        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                        // Send the intent to launch a new activity
                        startActivity(websiteIntent);
                    }
                });
            }
        } else {
            View progressBar = findViewById(R.id.progressBar);
            TextView emptyState = findViewById(R.id.emptyView);
            emptyState.setText("No internet Connection");
            emptyState.setVisibility(View.VISIBLE);
            earthquakeListView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);

        }
        Log.v("onCreate", "it has executed ");
        // Create a fake list of earthquake locations.


    }


    public Loader<List<earthquake>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPref.getString(getString(R.string.settings_min_magnitude_key), getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPref.getString(getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        Uri baseUri = Uri.parse(urls);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);
        return new EarthQuakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<earthquake>> loader, List<earthquake> data) {
        adapter.clear();
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
            earthquakeListView.setAdapter(adapter);
            earthquakeListView.setVisibility(View.VISIBLE);
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            earthquakeListView.setEmptyView(findViewById(R.id.emptyView));
            Log.i("onLoadFinished", "it has passed through this now");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<earthquake>> loader) {
        Log.i("onLoaderReset", "it has passes through onLoaderReset");
        adapter.clear();
    }






}