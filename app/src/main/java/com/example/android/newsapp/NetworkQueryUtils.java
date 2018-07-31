package com.example.android.newsapp;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DBM on 30/05/2017.
 */

public final class NetworkQueryUtils {
    //LOG_TAG
    private static final String LOG_TAG = NetworkQueryUtils.class.getSimpleName();
    //Test url for the query
    private static final String TEST_QUERY_URL = "http://content.guardianapis.com/search?order-by=newest&show-fields=byline&q=Ronaldo&api-key=test";

    private NetworkQueryUtils(){}

    public static List<Article> getArticles(String urlRequest){
        //Set a delay
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Create a List of articles
        List<Article> articles = new ArrayList<>();
        //Get an URL object using the String urlRequest
        URL url = createUrl(urlRequest);

        String response = "";
        try {
            //makeHttpRequest method returns a String object data type
            //Store String returned from makeHttpRequest in response variable
            response = makeHttpRequest(url);
        } catch (IOException e) {
            //Handle the IOException
            Log.e(LOG_TAG,"Problem making the HTTP request",e);
        }

        try {

            //Parse the obtained JSON response
            //build up a list of Article objects with the corresponding data.
            JSONObject root = new JSONObject(response);
            JSONObject resp = root.getJSONObject("response");
            JSONArray results = resp.getJSONArray("results");
            int length = results.length();
            //Since WE KNOW the length of the JSONArray results, we can use a for loop with i<features.length() as condition
            //For each item in the JSON response we get the respective data
            for(int i=0;i<results.length();i++){

                //Get JSONObject
                JSONObject article = results.getJSONObject(i);
                //Parse the JSON response from webTitle, sectionName, webUrl and webPublicationDate keys
                //String webTitle = article.getString("webTitle");
                String sectionName = article.getString("sectionName");
                String webUrl = article.getString("webUrl");
                String publicationDate = article.getString("webPublicationDate").substring(0,10);
                String webTitle;
                String author;
                String thumbnailUrl;

                try {
                    //Get JSONObject
                    JSONObject fields = article.getJSONObject("fields");
                    //Parse the JSON response from the byline key
                    try {
                        author = fields.getString("byline");
                    } catch(JSONException e){
                        author = "unknown author";
                    }

                    try{
                        webTitle = fields.getString("headline");
                    }
                    catch(JSONException e){
                        webTitle = "Unknown title";
                    }

                    try{
                        thumbnailUrl = fields.getString("thumbnail");
                    }
                    catch(JSONException e){
                        thumbnailUrl = "No image available";
                    }
                }
                catch (JSONException e){
                    //Handle Exception in case there is no data for fields JSON Object
                    author = "unknown author";
                    webTitle = "Unknown title";
                    thumbnailUrl = "No image available";
                }

                //Create the Article instance using the constructor and passing in the data parsed from the JSON response
                Article articleItem = new Article(webTitle,sectionName,author,publicationDate,webUrl,thumbnailUrl);

                //Add the Article to the List of articles
                articles.add(articleItem);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash.
            // Print a log message indicating a problem during parsing
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        // Return List of articles
        return articles;
    }



    /**
     * Returns new URL object from the given string URL.
     *
     * @return  URL object created from String url
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }


    /**
     * Make an HTTP request to the given URL
     *
     * @return the JSON response
     */
    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }


        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200)
            { inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);}
            else{
                Log.e("QueryUtils","The Status Code is: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            //Handle the exception
            Log.e("QueryUtils","Problem retrieving the earthquake JSON results.",e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                //Function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }


    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    public static String readFromStream(InputStream inputStream) throws IOException {
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
