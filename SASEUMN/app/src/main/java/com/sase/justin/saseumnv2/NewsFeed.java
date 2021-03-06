
package com.sase.justin.saseumnv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsFeed extends ListFragment implements FeedInterface {

    private List<NewsPojo> newsList = new ArrayList<>();
    private TextView updateText;
    private DateParser dateParser;
    private TextParser textParser;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View output = inflater.inflate(R.layout.activity_news_feed, container, false);
        Bundle newsBundle = this.getArguments();
        String newsData;

        if( newsBundle != null) {
            newsData = newsBundle.getString("newsData");
        }
        else {
            Toast.makeText(getActivity(), "Cannot obtain News Data!",
                    Toast.LENGTH_LONG).show();
            newsData = "[]";
        }

        updateText = (TextView) output.findViewById(R.id.newsUpdateText);
        dateParser = new DateParser();
        textParser = new TextParser(getActivity());

        try {
            // This checks if the JSON Array can be obtained from the string.
            JSONArray newsArray = new JSONArray(newsData);
            String formatDate = dateParser.getUpdateText();

            newsList = loadNewsFromJson(newsArray);
            textParser.updateTextFiles(newsData, formatDate, "nwList.txt", "nwLastUpdate.txt");
            updateText.setText(formatDate);
        } catch (JSONException e) {
            readOperation();
        } catch (IOException e) {
            Log.d("File nwList.txt: ", e.getLocalizedMessage());
            e.printStackTrace();
        }

        Collections.sort(newsList, new NewsPojo());
        setAdapter();

        return output;
    }

    public void readOperation() {
        String failedUpdate;

        try {
            String fileJson = textParser.readFromFile("nwList.txt");
            JSONArray newsArray = new JSONArray(fileJson);
            newsList = loadNewsFromJson(newsArray);

            String lastUpdate = textParser.readFromFile("nwLastUpdate.txt");
            updateText.setText(lastUpdate);
        } catch (JSONException | IOException e1) {
            failedUpdate = "Last Updated: N/A (Unable to load data!)";
            updateText.setText(failedUpdate);
        }
    }

    public void setAdapter() {
        ArrayAdapter m_nwAdapter = new NewsAdapter(getActivity(), newsList);
        setListAdapter(m_nwAdapter);
    }

    private List<NewsPojo> loadNewsFromJson(JSONArray jArray) {
        List<NewsPojo> nwOutput = new ArrayList<>();

        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject jsonNews = jArray.getJSONObject(i);
                NewsPojo news = new NewsPojo();
                String getResultDate = jsonNews.getString("Date");

                news.setTitle(jsonNews.getString("Title"));
                news.setDate(getResultDate);
                news.setContent(jsonNews.getString("Content"));
//                news.setImageUrl(jsonNews.getString("ImageUrl"));
                news.setDateID(dateParser.processDate(getResultDate));

                nwOutput.add(news);
            } catch (JSONException e) {
                e.printStackTrace();
                break;
            }
        }

        return nwOutput;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), NewsDetail.class);
        ListAdapter adapter = getListAdapter();
        NewsPojo nPojo = (NewsPojo) adapter.getItem(position);

        intent.putExtra("Title", nPojo.getTitle());
        intent.putExtra("Date", nPojo.getDate());
//        intent.putExtra("ImageUrl", nPojo.getImageUrl());
        intent.putExtra("Content", nPojo.getContent());

        startActivity(intent);
    }
}
