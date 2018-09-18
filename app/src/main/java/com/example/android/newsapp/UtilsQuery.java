package com.example.android.newsapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class UtilsQuery {
    public List<Report> fetchDataFromURL(String url) {
        List<Report> reportList = null;
        String jsonString = null;
        if (url == null || url.isEmpty()) {
            return null;
        }
        URL urlObject = makeURLObject(url);
        try {
            jsonString = makeHTTPConnect(urlObject);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("UtilsQuery", "fetchDataFromURL: ", e);
        }
        Log.e("util", "the response json is : " + jsonString);
        reportList = extraReportsFromJson(jsonString);
        return reportList;

    }

    public String extraFromInputstream(InputStream inputStream) {
        StringBuilder builderJson = new StringBuilder();
        if (inputStream == null) {
            return null;
        }
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                builderJson.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("UtilsQuery", "extraStringFromInputstream:  error in read from bufferd reader first line", e);
        } finally {

        }

        return builderJson.toString();
    }

    public String makeHTTPConnect(URL url) throws IOException {
        String jsonString = null;
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        if (url == null) {
            return jsonString;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(20000);
            urlConnection.setConnectTimeout(25000);
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonString = extraFromInputstream(inputStream);
                Log.i("test", "makeHTTPConnect: " + jsonString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return jsonString;
    }

    public URL makeURLObject(String url) {
        URL urlObject = null;
        if (url == null) {
            return urlObject;
        }
        try {
            urlObject = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e("UtilsQuery", "makeURLObject:  error in make url", e);
        }
        return urlObject;
    }

    public List<Report> extraReportsFromJson(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        List<Report> reportList = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(jsonString);
            JSONObject response = root.getJSONObject("response");
            JSONArray resultArray = response.getJSONArray("results");
            String authorName = "";
            String bitmapUrl = "";
            for (int i = 0; i < resultArray.length(); i++) {

                JSONObject reportObject = resultArray.getJSONObject(i);

                String title = reportObject.optString("webTitle");
                String section = reportObject.optString("sectionName");
                String webURL = reportObject.optString("webUrl");
                String timePublish = reportObject.optString("webPublicationDate");

                JSONObject fields = reportObject.optJSONObject("fields");

                if (fields != null) {
                    authorName = fields.optString("byline");
                    bitmapUrl = fields.optString("thumbnail");
                } else {
                    authorName = null;
                    bitmapUrl = null;
                }

                //download from web image view
                Bitmap bitmap = downloadImageView(bitmapUrl);
                //get date and clock from time all by split tools
                String timeSplit[] = timePublish.split("T");

                String date = timeSplit[0];
                String clock = timeSplit[1];
                Report report = new Report(title, section, authorName, date, clock, webURL, bitmap);
                reportList.add(report);
            }
        } catch (JSONException e) {
            e.printStackTrace();

            Log.e("UtilsQuery", "extraReportsFromJson: error in parsing json to list of report", e);
        }
        return reportList;
    }

    public Bitmap downloadImageView(String urlImage) {
        URL urlObject = null;
        Bitmap bitmap = null;
        InputStream inputStream = null;
        if (urlImage == null) {
            return null;
        }
        try {
            urlObject = new URL(urlImage);
            inputStream = urlObject.openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

}
