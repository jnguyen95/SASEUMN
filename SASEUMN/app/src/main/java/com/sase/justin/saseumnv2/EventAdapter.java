package com.sase.justin.saseumnv2;

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

    private LayoutInflater _inflater;
    private List<EventPojo> _eventsList;
    private TextView _name;
    private TextView _room;
    private TextView _time;
    private TextView _date;

    public EventAdapter (Activity activity, List<EventPojo> events) {
        super(activity, R.layout.event_list_detail, events);
        _eventsList = events;
        _inflater = activity.getWindow().getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EventPojo anEvent = _eventsList.get(position);
        View indivEvent = _inflater.inflate(R.layout.event_list_detail, parent, false);
        _name = (TextView) indivEvent.findViewById(R.id.eventTitle);
        _time = (TextView) indivEvent.findViewById(R.id.timeText);
        _room = (TextView) indivEvent.findViewById(R.id.roomText);
        _date = (TextView) indivEvent.findViewById(R.id.eDateText);

        _name.setText(anEvent.getName());
        _time.setText(anEvent.getTime());
        _room.setText(anEvent.getLocation());
        _date.setText(anEvent.getDate());

        return indivEvent;
    }
}
