package com.example.earthquake;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /**
     * Sample JSON response for a USGS query
     */
    private final String USER_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    /**
     * Create a private constructor because no one should ever create a {@link //QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link //Earthquake} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<earthquake> extractEarthquakes(String url) throws IOException {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<earthquake> earthquakes_1 = new ArrayList<>();
        String json_Data = null;
        String mPlace;
        double mMag;
        String mUrl;
        URL fetchingURL = createUrl(url);
        try {
            json_Data = makeHTTPrequest(fetchingURL);
        } catch (IOException e) {
            Log.e("extract DAta", e.toString());
        }
        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON

        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject jsonObject = new JSONObject(json_Data);
            //System.out.println(jsonObject.optJSONArray("features").length());
            for (int i = 0; i < jsonObject.getJSONArray("features").length(); i++) {
                System.out.println(jsonObject.optJSONArray("features").getJSONObject(i).optJSONObject("properties").get("time").getClass().getName());
                mMag = jsonObject.getJSONArray("features").optJSONObject(i).getJSONObject("properties").getDouble("mag");
                mPlace = jsonObject.getJSONArray("features").optJSONObject(i).getJSONObject("properties").getString("place");
                mUrl = jsonObject.getJSONArray("features").optJSONObject(i).getJSONObject("properties").getString("url");
                long date = jsonObject.getJSONArray("features").optJSONObject(i).optJSONObject("properties").getLong("time");
                earthquakes_1.add(new earthquake(mMag, mPlace, date, mUrl));

            }
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes_1;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
            return url;
        } catch (MalformedURLException e) {
            Log.e("MALFormedUrl Exception", String.valueOf(e));
        }
        return url;
    }

    private static String makeHTTPrequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection requestConnection = null;
        InputStream inputStream = null;
        try {
            requestConnection = (HttpURLConnection) url.openConnection();
            requestConnection.setRequestMethod("GET");
            requestConnection.setConnectTimeout(10000000);
            requestConnection.setReadTimeout(15000000);
            requestConnection.connect();
            if (requestConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = requestConnection.getInputStream();
                jsonResponse = readInputStream(inputStream);
            } else {
                Log.e("Quesry Utils", "connection failed");
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (requestConnection != null) {
                requestConnection.disconnect();
            }
        }
        return jsonResponse;
    }

    private static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }
}