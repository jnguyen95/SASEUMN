package com.sase.justin.saseumnv2;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Justin on 6/25/2015.
 */
public class GetDataParser {
    public GetDataParser() {}

    public String GET(String urlInput) throws IOException {
        URL url = new URL(urlInput);
        String result;
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        try {
            urlConnection.setRequestMethod("GET");
            InputStream in = urlConnection.getInputStream();
            if(in != null) {
                result = convertInputStreamToString(in);
            }
            else {
                result = "";
            }
        } finally {
            urlConnection.disconnect();
        }
        return result;
    }

    public boolean isValidJsonObject(String result) {
        try {
            JSONObject array = new JSONObject(result);
        } catch (JSONException e) {
            return false;
        }

        return true;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder resultStringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null)
            resultStringBuilder.append(line);
        inputStream.close();
        return resultStringBuilder.toString();
    }
}
