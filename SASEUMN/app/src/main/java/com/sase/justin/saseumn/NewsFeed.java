package com.sase.justin.saseumn;

import android.content.Intent;
import android.os.AsyncTask;
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

public class NewsFeed extends ListFragment {

    private List<NewsPojo> m_newsList = new ArrayList<>();
    private TextView updateText;
    private ArrayAdapter m_nwAdapter;
    private BGOperations m_bgParser;
    private DateOperations m_dateParser;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View output = inflater.inflate(R.layout.activity_news_feed, container, false);

        // Initialize the variables.
        updateText = (TextView) output.findViewById(R.id.newsUpdateText);
        m_dateParser = new DateOperations();
        m_bgParser = new BGOperations(getActivity());

        // And make the call.
        String currentDate = m_dateParser.getCurrentDate();
        new HttpAsyncTask().execute("HTTP://www.saseumn.org/API/news.php?msg=query&byDate=" + currentDate + "&timeframe=past");

        return output;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return BGOperations.GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            try {
                // This checks if the JSON Array can be obtained from the string.
                JSONArray newsArray = new JSONArray(result);
                String formatDate = m_dateParser.getUpdateText();

                m_newsList = mLoadNewsFromJson(newsArray);
                m_bgParser.updateTextFiles(result, formatDate, "nwList.txt", "nwLastUpdate.txt");
                updateText.setText(formatDate);
            } catch (JSONException e) {
                readOperation();
            } catch (IOException e) {
                Log.d("File nwList.txt: ", e.getLocalizedMessage());
                e.printStackTrace();
            }

            Collections.sort(m_newsList, new NewsPojo());
            setAdapter();
        }
    }

    private void readOperation()
    {
        try {
            String fileJson = m_bgParser.mReadFromFile("nwList.txt");
            JSONArray newsArray = new JSONArray(fileJson);
            m_newsList = mLoadNewsFromJson(newsArray);

            String lastUpdate = m_bgParser.mReadFromFile("nwLastUpdate.txt");
            updateText.setText(lastUpdate);
        } catch (JSONException e1) {
            Toast.makeText(getActivity(), "Unable to Parse Text! Unable to load from File!", Toast.LENGTH_SHORT).show();
        } catch (IOException e1) {
            Toast.makeText(getActivity(), "Files Not Found. Unable to load from File!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter()
    {
        m_nwAdapter = new NewsAdapter(getActivity(), m_newsList);
        setListAdapter(m_nwAdapter);
    }

    private List<NewsPojo> mLoadNewsFromJson(JSONArray jArray)
    {
        List<NewsPojo> nwOutput = new ArrayList<>();

        for (int i = 0; i < jArray.length(); i++)
        {
            try {
                JSONObject jsonNews = jArray.getJSONObject(i);
                NewsPojo news = new NewsPojo();
                String getResultDate = jsonNews.getString("Date");

                news.setTitle(jsonNews.getString("Title"));
                news.setDate(getResultDate);
                news.setContent(jsonNews.getString("Content"));
//                news.setImageUrl(jsonNews.getString("ImageUrl"));
                news.setDateID(m_dateParser.processDate(getResultDate));

                nwOutput.add(news);
            } catch (JSONException e) {
                e.printStackTrace();
                break;
            }
        }

        return nwOutput;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        Intent intent = new Intent(getActivity(), NewsDetail.class);
        ListAdapter adapter = getListAdapter();
        NewsPojo nPojo = (NewsPojo) adapter.getItem(position);

        intent.putExtra("Title", nPojo.getTitle());
        intent.putExtra("Date", nPojo.getDate());
//        intent.putExtra("ImageUrl", nPojo.getImageUrl());
        intent.putExtra("Content", nPojo.getContent());

        startActivity(intent);
    }

//    @Override
//    public void setUserVisibleHint(boolean visible) {
//        super.setUserVisibleHint(visible);
//        if (visible)
//        {
//            updateText = (TextView) getActivity().findViewById(R.id.newsUpdateText);
//            String currentDate = m_dateParser.getCurrentDate();
//            new HttpAsyncTask().execute("HTTP://new-sase-dahnny012.c9.io/API/news.php?msg=query&byDate=" + currentDate + "&timeframe=past");
//        }
//    }
}
