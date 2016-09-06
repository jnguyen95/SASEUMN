package com.sase.justin.saseumnv2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Justin on 11/28/2015.
 */
public class ConnectAdapter extends ArrayAdapter
{
    private LayoutInflater layoutInflater;
    private List<ConnectPojo> connectPojoList;

    public ConnectAdapter(Activity activity, List<ConnectPojo> connectPojoList) {
        super(activity, R.layout.connect_list_detail, connectPojoList);
        this.connectPojoList = connectPojoList;
        layoutInflater = activity.getWindow().getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View indivConnect = layoutInflater.inflate(R.layout.connect_list_detail, parent, false);

        ConnectPojo cPojo = connectPojoList.get(position);

        TextView descText = (TextView) indivConnect.findViewById(R.id.connectDescriptionText);
        ImageView connectImage = (ImageView) indivConnect.findViewById(R.id.connectImage);

        descText.setText(cPojo.getDescriptionText());
        connectImage.setImageResource(cPojo.getImageViewId());

        return indivConnect;
    }
}
