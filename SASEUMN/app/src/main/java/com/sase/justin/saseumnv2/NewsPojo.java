package com.sase.justin.saseumnv2;

import java.util.Comparator;

/**
 * Created by Justin on 7/27/2015.
 */
public class NewsPojo implements Comparator<NewsPojo>
{
    String title;
    String date;
    String content;
    String imageUrl;
    long dateID;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
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
    public int compare(NewsPojo np1, NewsPojo np2) {
        long dateIDnp1 = np1.getDateID();
        long dateIDnp2 = np2.getDateID();
        return (int) (dateIDnp2 - dateIDnp1);
    }
}
