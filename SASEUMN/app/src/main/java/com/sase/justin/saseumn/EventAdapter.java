package com.sase.justin.saseumn;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Justin on 6/2/2015.
 */
public class EventAdapter extends ArrayAdapter {

    private LayoutInflater m_inflater;
    private List<EventPojo> m_eventsList;
    private TextView name;
    private TextView room;
    private TextView time;
    private TextView date;

    public EventAdapter (Activity activity, List<EventPojo> events)
    {
        super(activity, R.layout.event_list_detail, events);
        m_eventsList = events;
        m_inflater = activity.getWindow().getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        EventPojo anEvent = m_eventsList.get(position);
        View indivEvent = m_inflater.inflate(R.layout.event_list_detail, parent, false);
        name = (TextView) indivEvent.findViewById(R.id.eventTitle);
        time = (TextView) indivEvent.findViewById(R.id.timeText);
        room = (TextView) indivEvent.findViewById(R.id.roomText);
        date = (TextView) indivEvent.findViewById(R.id.eDateText);

        name.setText(anEvent.getName());
        time.setText(anEvent.getTime());
        room.setText(anEvent.getLocation());
        date.setText(anEvent.getDate());

        return indivEvent;
    }
}
