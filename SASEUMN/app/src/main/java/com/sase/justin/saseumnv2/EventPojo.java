package com.sase.justin.saseumnv2;

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
    String endTime;
    String description;
    String fb;
    long dateID;

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

    public String getEndTime() {return endTime; }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
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

    public long getDateID()
    {
        return dateID;
    }

    public void setDateID(long dateID)
    {
        this.dateID = dateID;
    }

    @Override
    public int compare(EventPojo ep1, EventPojo ep2) {
        long dateIDep1 = ep1.getDateID();
        long dateIDep2 = ep2.getDateID();
        return (int) (dateIDep1 - dateIDep2);
    }
}
