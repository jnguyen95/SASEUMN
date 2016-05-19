package com.sase.justin.saseumnv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventsCalendar extends ListFragment implements FeedInterface {

    private List<EventPojo> _eventsList = new ArrayList<>();
    private BGOperations _bgParser;
    private DateOperations _dateParser;
    private TextOperations _textParser;
    private TextView _updateText;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View output = inflater.inflate(R.layout.activity_events_calendar, container, false);
        String eventData;

        // Initialize the Variables.
        _bgParser = new BGOperations(getActivity());
        _dateParser = new DateOperations();
        _textParser = new TextOperations(getActivity());
        _updateText = (TextView) output.findViewById(R.id.eventUpdateText);
        Bundle eventBundle = this.getArguments();

        if (eventBundle != null) {
            eventData = eventBundle.getString("eventData");
        }
        else {
            Toast.makeText(getActivity(), "Cannot obtain Event Data!",
                    Toast.LENGTH_LONG).show();
            eventData = "";
        }

        try {
            // This checks if the JSON Array can be obtained from the string.
            JSONArray eventArray = new JSONArray(eventData);
            String formatDate = _dateParser.getUpdateText();

            // If this returns a valid array, store all of these in a text file inside.
            _eventsList = loadEventsFromJson(eventArray);
            _textParser.updateTextFiles(eventData, formatDate, "evList.txt", "evLastUpdate.txt");
            _updateText.setText(formatDate);
        } catch (JSONException | NullPointerException e) {
            readOperation();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // This is done in the case that the API call, for whatever reason, does not load the correct events properly.
        Collections.sort(_eventsList, new EventPojo());
        processDateIDs();

        // If there are no events to be loaded from file (EG: A fresh app that is somehow offline after installation)
        // Then this will simply throw a printStackTrace() and continue on as planned.
        try {
            EventPojo nextEvent = _eventsList.get(0);
            _eventsList = _eventsList.subList(1, _eventsList.size());
            loadNextEvent(nextEvent, output);
        }
        catch (java.lang.IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        setAdapter();

        return output;
    }

    // This function is only called if the device cannot communicate with the API or is not online.
    // This loads the existing events that are stored in the file "evList.txt" from the last time the app was called.
    public void readOperation() {
        String failedUpdate;

        try {
            // If we can't obtain data from the server, load the local text files to display in the event calendar.
            String fileJson = _textParser.readFromFile("evList.txt");
            JSONArray eventArray = new JSONArray(fileJson);
            _eventsList = loadEventsFromJson(eventArray);

            // If successful, load the second text file containing the dates.
            String lastUpdate = _textParser.readFromFile("evLastUpdate.txt");
            _updateText.setText(lastUpdate);
        } catch (JSONException | IOException e1) {
            failedUpdate = "Last Updated: N/A (Unable to load data!)";
            _updateText.setText(failedUpdate);
        }
    }

    public void setAdapter() {
        ArrayAdapter m_evAdapter = new EventAdapter(getActivity(), _eventsList);
        setListAdapter(m_evAdapter);
    }

    //Functions for Events. If a JSONArray exists from the API call, store each element in an EventPojo
    //This will then be used to display data to the user from the Events Calendar and the Event Detail.
    private List<EventPojo> loadEventsFromJson(JSONArray jArray) {
        List<EventPojo> eventOutput = new ArrayList<EventPojo>();
        String getOriginalEndTime;
        String getResultDate;
        String getResultTime;
        String endTime;

        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject jsonEvent = jArray.getJSONObject(i);
                EventPojo event = new EventPojo();

                getResultDate = jsonEvent.getString("Date");

                getOriginalEndTime = jsonEvent.getString("EndTime");
                getResultTime = _dateParser.convertTime(jsonEvent.getString("Time"));
                endTime = _dateParser.convertTime(getOriginalEndTime);

                event.setName(jsonEvent.getString("Name"));
                event.setDate(_dateParser.ifTomorrowEventDate(getResultDate));
                event.setDescription(jsonEvent.getString("Description"));
                event.setLocation(jsonEvent.getString("Location"));
                event.setTime(_dateParser.concatenateDates(getResultTime, endTime));
                event.setFb(jsonEvent.getString("FB"));
                event.setDateID(_dateParser.processDateAndTime(getResultDate, getOriginalEndTime.replace(":", "")));

                eventOutput.add(event);
            } catch (JSONException e) {
                e.printStackTrace();
                break;
            }
        }

        return eventOutput;
    }

    // This loads the next event under the "Next Events" in the view.
    private void loadNextEvent(final EventPojo nextPojo, View view) {
        TextView nextTitle = (TextView) view.findViewById(R.id.nextEventTitle);
        TextView nextDate = (TextView) view.findViewById(R.id.nextDateText);
        TextView nextRoom = (TextView) view.findViewById(R.id.nextRoomText);
        TextView nextTime = (TextView) view.findViewById(R.id.nextTimeText);

        final String nEvent = nextPojo.getName();
        final String nDate = nextPojo.getDate();
        final String nRoom = nextPojo.getLocation();
        final String nTime = nextPojo.getTime();

        // Make the relative layout clickable.
        RelativeLayout rlayout = (RelativeLayout) view.findViewById(R.id.nextEventLayout);
            rlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), EventDetail.class);

                    intent.putExtra("Name", nEvent);
                    intent.putExtra("Date", nDate);
                    intent.putExtra("Fb", nextPojo.getFb());
                    intent.putExtra("Location", nRoom);
                    intent.putExtra("Time", nTime);
                    intent.putExtra("Description", nextPojo.getDescription());

                    startActivity(intent);
                }
            });


            nextTitle.setText(nEvent);
            nextDate.setText(nDate);
            nextRoom.setText(nRoom);
            nextTime.setText(nTime);
    }

    // ProcessDateIDs() is used to get rid of events that escaped the link API call. For example,
    // if there is an event dated to September 5th, 2015 and today was September 12th, 2015, the
    // September 5th event will be removed from the list.
    private void processDateIDs() {
        long pojoDateID;
        EventPojo ePojo;
        boolean ifDone = false;

        String currentDate = _dateParser.getCurrentDateAndTime();

        long currentDateID = Long.parseLong(currentDate);
        while (!ifDone && !_eventsList.isEmpty()) {
            ePojo = _eventsList.get(0);
            pojoDateID = ePojo.getDateID();
            if (pojoDateID < currentDateID) {
                _eventsList.remove(0);
            }
            else {
                ifDone = true;
            }
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), EventDetail.class);
        ListAdapter adapter = getListAdapter();
        EventPojo ePojo = (EventPojo) adapter.getItem(position);

        intent.putExtra("Name", ePojo.getName());
        intent.putExtra("Date", ePojo.getDate());
        intent.putExtra("Fb", ePojo.getFb());
        intent.putExtra("Location", ePojo.getLocation());
        intent.putExtra("Time", ePojo.getTime());
        intent.putExtra("Description",ePojo.getDescription());

        startActivity(intent);
    }
}
