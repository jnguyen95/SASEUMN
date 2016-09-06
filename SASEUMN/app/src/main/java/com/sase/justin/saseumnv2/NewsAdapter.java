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
    private List<NewsPojo> news;
    private LayoutInflater _inflater;
    //    private TextView imageUrl;

    public NewsAdapter(Activity activity, List<NewsPojo> news) {
        super(activity, R.layout.news_list_detail, news);
        this.news = news;
        _inflater = activity.getWindow().getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsPojo aNews = news.get(position);
        View indivEvent = _inflater.inflate(R.layout.news_list_detail, parent, false);

        TextView title = (TextView) indivEvent.findViewById(R.id.titleText);
        TextView content = (TextView) indivEvent.findViewById(R.id.contentText);
        TextView date = (TextView) indivEvent.findViewById(R.id.nDateText);

        title.setText(aNews.getTitle());
        content.setText(Html.fromHtml(aNews.getContent()));
        date.setText(aNews.getDate());

        return indivEvent;
    }

}
