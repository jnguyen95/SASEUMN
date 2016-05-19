package com.sase.justin.saseumnv2;

/**
 * Created by Justin on 11/29/2015.
 */
public class ConnectPojo
{
    int imageViewId;
    String descriptionText;
    String connectUrl;

    public String getDescriptionText()
    {
        return descriptionText;
    }

    public void setDescriptionText(String desc)
    {
        descriptionText = desc;
    }

    public String getConnectUrl()
    {
        return connectUrl;
    }

    public void setUrl(String url)
    {
        connectUrl = url;
    }

    public int getImageViewId()
    {
        return imageViewId;
    }

    public void setImageViewId(int id)
    {
        imageViewId = id;
    }
}
