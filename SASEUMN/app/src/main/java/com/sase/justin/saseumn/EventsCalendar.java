package com.sase.justin.saseumn;

import android.content.Intent;
import android.os.AsyncTask;
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

public class EventsCalendar extends ListFragment {

    private List<EventPojo> m_eventsList = new ArrayList<>();
    private ArrayAdapter    m_evAdapter;
    private BGOperations    m_bgParser;
    private DateOperations  m_dateParser;

    private TextView updateText;
    private TextView nextTitle;
    private TextView nextDate;
    private TextView nextRoom;
    private TextView nextTime;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View output = inflater.inflate(R.layout.activity_events_calendar, container, false);

        // Initialize the Variables.
        m_bgParser = new BGOperations(getActivity());
        m_dateParser = new DateOperations();
        updateText = (TextView) output.findViewById(R.id.eventUpdateText);

        // Now make the calls.
        String currentDate = m_dateParser.getCurrentDate();
        new HttpAsyncTask().execute("http://www.saseumn.org/API/event.php?msg=query&byDate=" + currentDate + "&timeframe=future");
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
//                 This checks if the JSON Array can be obtained from the string.
                JSONArray eventArray = new JSONArray(result);
                String formatDate = m_dateParser.getUpdateText();

                // If this returns a valid array, store all of these in a text file inside.
                m_eventsList = mLoadEventsFromJson(eventArray);
                m_bgParser.updateTextFiles(result, formatDate, "evList.txt", "evLastUpdate.txt");
                updateText.setText(formatDate);
            } catch (JSONException e) {
                readOperation();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // This is done in the case that the API call, for whatever reason, does not load the correct events properly.
            Collections.sort(m_eventsList, new EventPojo());
            processDateIDs();

            // If there are no events to be loaded from file (EG: A fresh app that is somehow offline after installation)
            // Then this will simply throw a printStackTrace() and continue on as planned.
            try {
                EventPojo nextEvent = m_eventsList.get(0);
                m_eventsList = m_eventsList.subList(1, m_eventsList.size());
                loadNextEvent(nextEvent);
            }
            catch (java.lang.IndexOutOfBoundsException e)
            {
                e.printStackTrace();
            }
            setAdapter();
        }
    }

    // This function is only called if the device cannot communicate with the API or is not online.
    // This loads the existing events that are stored in the file "evList.txt" from the last time the app was called.
    private void readOperation() {
        try {
            // If we can't obtain data from the server, load the local text files to display in the event calendar.
            String fileJson = m_bgParser.mReadFromFile("evList.txt");
            JSONArray eventArray = new JSONArray(fileJson);
            m_eventsList = mLoadEventsFromJson(eventArray);

            // If successful, load the second text file containing the dates.
            String lastUpdate = m_bgParser.mReadFromFile("evLastUpdate.txt");
            updateText.setText(lastUpdate);
        } catch (JSONException e1) {
            Toast.makeText(getActivity(), "Unable to load events from File!", Toast.LENGTH_SHORT).show();
        } catch (IOException e1) {
            Toast.makeText(getActivity(), "Files Not Found", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter()
    {
        m_evAdapter = new EventAdapter(getActivity(), m_eventsList);
        setListAdapter(m_evAdapter);
    }

    //Functions for Events. If a JSONArray exists from the API call, store each element in an EventPojo
    //This will then be used to display data to the user from the Events Calendar and the Event Detail.
    private List<EventPojo> mLoadEventsFromJson(JSONArray jArray)
    {
        List<EventPojo> eventOutput = new ArrayList<EventPojo>();
        String getResultDate;
        String getResultTime;

        for (int i = 0; i < jArray.length(); i++)
        {
            try {
                JSONObject jsonEvent = jArray.getJSONObject(i);
                EventPojo event = new EventPojo();

                getResultDate = jsonEvent.getString("Date");
                getResultTime = jsonEvent.getString("Time");

                event.setName(jsonEvent.getString("Name"));
                event.setDate(m_dateParser.ifTomorrowEventDate(getResultDate));
                event.setDescription(jsonEvent.getString("Description"));
                event.setLocation(jsonEvent.getString("Location"));
                event.setTime(m_dateParser.convertTime(getResultTime));
                event.setFb(jsonEvent.getString("FB"));
                event.setDateID(m_dateParser.processDate(getResultDate));

                eventOutput.add(event);
            } catch (JSONException e) {
                e.printStackTrace();
                break;
            }
        }

        return eventOutput;
    }

    // This loads the next event under the "Next Events" in the view.
    private void loadNextEvent(final EventPojo nextPojo)
    {
        nextTitle = (TextView) getActivity().findViewById(R.id.nextEventTitle);
        nextDate  = (TextView) getActivity().findViewById(R.id.nextDateText);
        nextRoom  = (TextView) getActivity().findViewById(R.id.nextRoomText);
        nextTime  = (TextView) getActivity().findViewById(R.id.nextTimeText);

        final String nEvent = nextPojo.getName();
        final String nDate = nextPojo.getDate();
        final String nRoom = nextPojo.getLocation();
        final String nTime = nextPojo.getTime();

        // Make the relative layout clickable.
        RelativeLayout rlayout = (RelativeLayout) getActivity().findViewById(R.id.nextEventLayout);
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
    private void processDateIDs()
    {
        int pojoDateID;
        EventPojo ePojo;
        boolean ifDone = false;

        String currentDate = m_dateParser.getCurrentDate();
        currentDate = currentDate.replace("-","");

        int currentDateID = Integer.parseInt(currentDate);
        while (!ifDone && !m_eventsList.isEmpty())
        {
            ePojo = m_eventsList.get(0);
            pojoDateID = ePojo.getDateID();
            if (pojoDateID < currentDateID)
            {
                m_eventsList.remove(0);
            }
            else
            {
                ifDone = true;
            }
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
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
