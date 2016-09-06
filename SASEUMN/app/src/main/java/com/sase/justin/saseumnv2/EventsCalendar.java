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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*

 Events Calendar uses the Facebook Graph API for its calls. This implementation
 changes depending on website functionality. As of 2016-17, this uses the Facebook Graph API.

*/
public class EventsCalendar extends ListFragment implements FeedInterface {

    private List<EventPojo> eventsList = new ArrayList<>();
    private DateParser dateParser;
    private TextParser textParser;
    private TextView updateText;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View output = inflater.inflate(R.layout.activity_events_calendar, container, false);
        String eventData;

        // Initialize the Variables.
        dateParser = new DateParser();
        textParser = new TextParser(getActivity());
        updateText = (TextView) output.findViewById(R.id.eventUpdateText);
        Bundle eventBundle = this.getArguments();

        if (eventBundle != null) {
            eventData = eventBundle.getString("fbEventData");
        }
        else {
            Toast.makeText(getActivity(), "Cannot obtain Event Data!",
                    Toast.LENGTH_LONG).show();
            eventData = "";
        }

        try {
            // This checks if the JSON Array can be obtained from the string.
            JSONObject jsonData = new JSONObject(eventData);
            JSONArray eventArray = jsonData.getJSONArray("data");
            // If this returns a valid array, store all of these in a text file inside.
            eventsList = loadEventsFromJson(eventArray);

            String formatDate = dateParser.getUpdateText();
            textParser.updateTextFiles(eventData, formatDate, "evList.txt", "evLastUpdate.txt");
            updateText.setText(formatDate);
        } catch (JSONException | NullPointerException e) {
            readOperation();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // This is done in the case that the API call, for whatever reason, does not load the correct events properly.
        Collections.sort(eventsList, new EventPojo());
        processDateIDs();

        // If there are no events to be loaded from file (EG: A fresh app that is somehow offline after installation)
        // Then this will simply throw a printStackTrace() and continue on as planned.
        try {
            EventPojo nextEvent = eventsList.get(0);
            eventsList = eventsList.subList(1, eventsList.size());
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
            String fileJson = textParser.readFromFile("evList.txt");
            JSONArray eventArray = new JSONArray(fileJson);
            eventsList = loadEventsFromJson(eventArray);

            // If successful, load the second text file containing the dates.
            String lastUpdate = textParser.readFromFile("evLastUpdate.txt");
            updateText.setText(lastUpdate);
        } catch (JSONException | IOException e1) {
            failedUpdate = "Last Updated: N/A (Unable to load data!)";
            updateText.setText(failedUpdate);
        }
    }

    public void setAdapter() {
        ArrayAdapter m_evAdapter = new EventAdapter(getActivity(), eventsList);
        setListAdapter(m_evAdapter);
    }

    //Functions for Events. If a JSONArray exists from the API call, store each element in an EventPojo
    //This will then be used to display data to the user from the Events Calendar and the Event Detail.
    private List<EventPojo> loadEventsFromJson(JSONArray jArray) {
        List<EventPojo> eventOutput = new ArrayList<>();

        String getOriginalEndTime;
        String getOriginalStartTime;
        String beginDate;
        String beginTime;
        String endTime;

        Date fbBeginDate;
        Date fbEndDate;

        JSONObject locationJson;

        SimpleDateFormat fbTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ENGLISH);

        for (int i = 0; i < jArray.length(); i++) {
            try {
                JSONObject jsonEvent = jArray.getJSONObject(i);
                EventPojo event = new EventPojo();

                try {
                    event.setName(jsonEvent.getString("name"));
                    event.setDescription(jsonEvent.getString("description"));

                    locationJson = jsonEvent.getJSONObject("place");
                    event.setLocation(locationJson.getString("name"));

                    getOriginalStartTime = jsonEvent.getString("start_time");
                    fbBeginDate = fbTimeFormat.parse(getOriginalStartTime);
                    beginTime = dateParser.convertTime(dateParser.getTime(fbBeginDate));
                    beginDate = dateParser.getDate(fbBeginDate);

                    getOriginalEndTime = jsonEvent.getString("end_time");
                    fbEndDate = fbTimeFormat.parse(getOriginalEndTime);
                    endTime = dateParser.convertTime(dateParser.getTime(fbEndDate));

                    event.setDate(ifTomorrowEventDate(dateParser.getDate(fbBeginDate)));
                    event.setTime(dateParser.concatenateDates(beginTime, endTime));
                    event.setDateID(dateParser.processDateAndTime(beginDate, dateParser.getTime(fbEndDate)));
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                eventOutput.add(event);
            } catch (ParseException e) {
                e.printStackTrace();
                break;
            } catch (JSONException e) {
                e.printStackTrace();
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
        int i = 0;
        boolean ifDone = false;

        String currentDate = dateParser.getCurrentDateAndTime();
        long currentDateID = Long.parseLong(currentDate);

        while (i < eventsList.size()) {
            ePojo = eventsList.get(i);
            pojoDateID = ePojo.getDateID();
            System.out.println("Event: " + ePojo.getName());
            System.out.println("CurrentDateID: " + currentDateID + ", pojoDateID: " + pojoDateID);
            if (pojoDateID < currentDateID) {
                eventsList.remove(i);
            }
            else {
                i++;
            }
        }
    }

    public String ifTomorrowEventDate(String date) throws ParseException {
        String[] dateArray = date.split("-");

        int dateYear  = Integer.parseInt(dateArray[0]);
        int dateMonth = Integer.parseInt(dateArray[1]);
        int dateDay   = Integer.parseInt(dateArray[2]);

        String year = new SimpleDateFormat("yyyy", Locale.US).format(new Date());
        int iYear = Integer.parseInt(year);
        String month = new SimpleDateFormat("MM", Locale.US).format(new Date());
        int iMonth = Integer.parseInt(month);
        String day = new SimpleDateFormat("dd", Locale.US).format(new Date());
        int iDay = Integer.parseInt(day);

        if (dateYear - iYear == 0) {
            if (dateMonth - iMonth == 0) {
                if (dateDay - iDay == 0) {
                    return "Today!";
                }
                else if (dateDay - iDay == 1) {
                    return "Tomorrow!";
                }
            }
        }

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date inputDate = inputFormat.parse(date);
        return dateParser.getUSDate(inputDate);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), EventDetail.class);
        ListAdapter adapter = getListAdapter();
        EventPojo ePojo = (EventPojo) adapter.getItem(position);

        intent.putExtra("Name", ePojo.getName());
        intent.putExtra("Date", ePojo.getDate());
        intent.putExtra("Location", ePojo.getLocation());
        intent.putExtra("Time", ePojo.getTime());
        intent.putExtra("Description",ePojo.getDescription());

        startActivity(intent);
    }
}
