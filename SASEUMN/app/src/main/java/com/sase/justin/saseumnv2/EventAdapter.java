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

    private LayoutInflater inflater;
    private List<EventPojo> eventsList;

    public EventAdapter (Activity activity, List<EventPojo> events) {
        super(activity, R.layout.event_list_detail, events);
        eventsList = events;
        inflater = activity.getWindow().getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EventPojo anEvent = eventsList.get(position);
        View indivEvent = inflater.inflate(R.layout.event_list_detail, parent, false);
        TextView name = (TextView) indivEvent.findViewById(R.id.eventTitle);
        TextView time = (TextView) indivEvent.findViewById(R.id.timeText);
        TextView room = (TextView) indivEvent.findViewById(R.id.roomText);
        TextView date = (TextView) indivEvent.findViewById(R.id.eDateText);

        name.setText(anEvent.getName());
        time.setText(anEvent.getTime());
        room.setText(anEvent.getLocation());
        date.setText(anEvent.getDate());

        return indivEvent;
    }
}
