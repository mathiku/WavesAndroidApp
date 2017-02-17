package com.example.mat.wavesv01;
import android.util.Base64;
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
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import static com.example.mat.wavesv01.MainActivity.LOG_TAG;

/**
 * Created by Mat on 16-02-2017.
 */

public class QueryUtils {
    //private String stringUrl = "https://backend.sigfox.com/api/devices/4D2A38/messages";
    //private static final String SAMPLE_JSON_RESPONSE = "{\"paging\": {}, \"data\": [{\"device\": \"4D2A38\", \"snr\": \"12.73\", \"data\": \"6a756875\", \"linkQuality\": \"AVERAGE\", \"time\": 1486560838}, {\"device\": \"4D2A38\", \"snr\": \"50.58\", \"data\": \"6a756875\", \"linkQuality\": \"GOOD\", \"time\": 1486560756}, {\"device\": \"4D2A38\", \"snr\": \"19.51\", \"data\": \"6a756875\", \"linkQuality\": \"GOOD\", \"time\": 1486482690}, {\"device\": \"4D2A38\", \"snr\": \"17.69\", \"data\": \"0102030405060708090a0b0c\", \"linkQuality\": \"GOOD\", \"time\": 1486458095}]}";
    /**
     * Tom konstruktør. Holder kun statiske metoder.
     */
    private QueryUtils() {
    }



    public  static List<Messages> extractMessages(String messagesAsJSON) {

        List<Messages> messagesList = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(messagesAsJSON);
            JSONArray features = root.getJSONArray("data");

            for (int i = 0; i < features.length(); i++){
                JSONObject q = features.getJSONObject(i);
                String mag = q.getString("device");
                String pla = q.getString("data");
                Long tim = q.getLong("time");
                String url =q.getString("linkQuality");
                messagesList.add(new Messages(mag, pla, tim, url));

            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        // Return the list of earthquakes
        return messagesList;
    }


    /**
     * Copy/Paste af alle hjælpermetoder fra Google - Udacity - Networking GITHUB
     */
    public static List<Messages> fetchEarthquakeData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Messages> messagesList = extractMessages(jsonResponse);

        // Return the list of {@link Earthquake}s
        return messagesList;
    }


    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "no good URL",e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
       // Authenticator.setDefault(new Authenticator(){
       //     protected PasswordAuthentication getPasswordAuthentication() {
        //        return new PasswordAuthentication("myuser","mypass".toCharArray());
        //    }});
        final String basicAuth = "Basic " + Base64.encodeToString("5899927a3c878949439a4970:84f74c0ddaf90a46580d3085d116ac0b".getBytes(), Base64.NO_WRAP);
        try {

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);

            urlConnection.setRequestMethod("GET");

            urlConnection.setRequestProperty("Authorization",basicAuth);

            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }
    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}
