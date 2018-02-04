package com.example.shaimaaderbaz.earthquake.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.EventLog.Event;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.shaimaaderbaz.earthquake.R;
import com.example.shaimaaderbaz.earthquake.adapters.EearthquakeAdapter;
import com.example.shaimaaderbaz.earthquake.models.Earthquake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String URL_USGS = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2016-01-01&endtime=2016-01-31&minmagnitude=5&limit=10";
  //  URL url=new URL(URL_USGS) ;
  ListView earthquakeListView ;
    EearthquakeAdapter mAdapter;
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("", "Problem building the URL ", e);
        }
        return url;
    }

    public static String readFromStream(InputStream input) throws Exception
    {
        StringBuilder output =new StringBuilder();
        if(input !=null)
        {
            InputStreamReader inputStreamReader =new InputStreamReader(input, Charset.forName("UTF-8"));
            BufferedReader reader=new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line !=null)
            {
                output.append(line);
                line=reader.readLine();
            }
        }
        return output.toString();
    }

    public static String makeRequests (URL url)  throws Exception
    {
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        String jsonResponse="";
        if (url==null)
        {
            return jsonResponse;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);

            }
        }catch (Exception e)
        {

        }
        return jsonResponse;
    }

    private static ArrayList<Earthquake> extractFeatureFromJson(String earthquakeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(earthquakeJSON)) {
            return null;
        }

        ArrayList<Earthquake> earthquakes = new ArrayList<>();

        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(earthquakeJSON);


            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");
            for (int i = 0; i < earthquakeArray.length(); i++) {

                JSONObject currentEarthquake = earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquake.getJSONObject("properties");
                double magnitude = properties.getDouble("mag");
                String location = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");
                Earthquake earthquake = new Earthquake(magnitude, location, time, url);
                earthquakes.add(earthquake);

            }
        } catch (JSONException e) {
            Log.e("", "Problem parsing the earthquake JSON results", e);
        }
        return earthquakes;
    }

  public static ArrayList<Earthquake> fetchEarthquakeData(String requestUrl) {

        URL url = createUrl(requestUrl);


        String jsonResponse = null;
        try {
            jsonResponse = makeRequests(url);
        } catch (IOException e) {
            Log.e("", "Problem making the HTTP request.", e);
        } catch (Exception e) {
            e.printStackTrace();
        }


      ArrayList<Earthquake> earthquakes = extractFeatureFromJson(jsonResponse);


        return earthquakes;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        earthquakeListView = (ListView) findViewById(R.id.activity_main);
        URL URLus=createUrl(URL_USGS);
        EarthquakeAsyncTask task=new EarthquakeAsyncTask(this);
        task.execute(URLus);

        ///

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Earthquake currentEarthquake = mAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });



    }

    private class EarthquakeAsyncTask extends AsyncTask<URL, Integer ,ArrayList<Earthquake>>
    {
        private Activity mContext;
        public EarthquakeAsyncTask(Activity activity) {
            super();
            mContext=activity;
        }
     @Override
     protected ArrayList<Earthquake> doInBackground (URL ... Url)
     {

             ArrayList<Earthquake> result = fetchEarthquakeData(URL_USGS);


         return result;
     }
      protected void onPostExecute(ArrayList<Earthquake> result)
      {

         if (result == null)
         {
            return;
         }
          mAdapter = new EearthquakeAdapter(mContext,result);
          earthquakeListView.setAdapter(mAdapter);

        }
    }
}
