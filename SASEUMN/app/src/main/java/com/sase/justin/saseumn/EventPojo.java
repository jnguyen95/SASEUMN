package com.sase.justin.saseumn;

import java.util.Comparator;

/**
 * Created by Justin on 6/25/2015.
 */
public class EventPojo implements Comparator<EventPojo>
{
    String name;
    String date;
    String location;
    String time;
    String description;
    String fb;
    int dateID;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getFb()
    {
        return fb;
    }

    public void setFb(String fb)
    {
        this.fb = fb;
    }

    public int getDateID()
    {
        return dateID;
    }

    public void setDateID(int dateID)
    {
        this.dateID = dateID;
    }

    @Override
    public int compare(EventPojo ep1, EventPojo ep2) {
        return ep1.dateID - ep2.dateID;
    }
}
