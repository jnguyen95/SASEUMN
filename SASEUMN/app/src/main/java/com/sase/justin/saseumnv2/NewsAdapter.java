package com.sase.justin.saseumnv2;

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
    private List<NewsPojo> _news;
    private LayoutInflater _inflater;
    private TextView _title;
    private TextView _content;
    private TextView _date;
//    private TextView imageUrl;

    public NewsAdapter(Activity activity, List<NewsPojo> news) {
        super(activity, R.layout.news_list_detail, news);
        _news = news;
        _inflater = activity.getWindow().getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsPojo aNews = _news.get(position);
        View indivEvent = _inflater.inflate(R.layout.news_list_detail, parent, false);

        _title = (TextView) indivEvent.findViewById(R.id.titleText);
        _content = (TextView) indivEvent.findViewById(R.id.contentText);
        _date = (TextView) indivEvent.findViewById(R.id.nDateText);

        _title.setText(aNews.getTitle());
        _content.setText(Html.fromHtml(aNews.getContent()));
        _date.setText(aNews.getDate());

        return indivEvent;
    }

}
