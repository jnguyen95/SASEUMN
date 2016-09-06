package com.sase.justin.saseumnv2;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by Justin on 5/16/2016.
 */
public class TextParser {

    private Context context;
    public TextParser(Context context) {
        this.context = context;

    }

    public void updateTextFiles(String result, String update, String listFile, String dateFile) throws IOException {
        createAndSaveFile(result, listFile);
        createAndSaveFile(update, dateFile);
    }

    public void createAndSaveFile(String json, String fileName) throws IOException {
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
        outputStreamWriter.write(json);
        outputStreamWriter.close();
    }

    public String readFromFile(String fileName) throws IOException {
        FileInputStream fis = context.openFileInput(fileName);
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
