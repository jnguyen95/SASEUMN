package com.sase.justin.saseumnv2;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Justin on 6/25/2015.
 */
public class BGOperations {
    // TODO: Move Text-related functions over to TextOperations. Change BGOperations to a Sinngleton.
    Context m_context;
    public BGOperations() {}
    public BGOperations(Context context)
    {
        this.m_context = context;
    }

    // API CALLS HERE //
    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    public boolean isValidJsonArray(String result) {
        try {
            JSONArray array = new JSONArray(result);
        } catch (JSONException e) {
            return false;
        }

        return true;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    public void updateTextFiles(String result, String update, String listFile, String dateFile) throws IOException {
        mCreateAndSaveFile(result, listFile);
        mCreateAndSaveFile(update, dateFile);
    }

    // This has been moved to TextOperations
    public void mCreateAndSaveFile(String json, String fileName) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(m_context.openFileOutput(fileName, Context.MODE_PRIVATE));
        outputStreamWriter.write(json);
        outputStreamWriter.close();
    }

    public String mReadFromFile(String fileName) throws IOException {
        FileInputStream fis = m_context.openFileInput(fileName);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

}
