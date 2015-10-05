package com.sase.justin.saseumn;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Justin on 7/27/2015.
 */
public class NewsAdapter extends ArrayAdapter
{
    private List<NewsPojo> m_news;
    private LayoutInflater m_inflater;
    private TextView title;
    private TextView content;
    private TextView date;
//    private TextView imageUrl;

    public NewsAdapter(Activity activity, List<NewsPojo> news)
    {
        super(activity, R.layout.news_list_detail, news);
        this.m_news = news;
        m_inflater = activity.getWindow().getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        NewsPojo aNews = m_news.get(position);
        View indivEvent = m_inflater.inflate(R.layout.news_list_detail, parent, false);

        title = (TextView) indivEvent.findViewById(R.id.titleText);
        content = (TextView) indivEvent.findViewById(R.id.contentText);
        date = (TextView) indivEvent.findViewById(R.id.nDateText);

        title.setText(aNews.getTitle());
        content.setText(Html.fromHtml(aNews.getContent()));
        date.setText(aNews.getDate());

        return indivEvent;
    }

}
