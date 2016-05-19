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
    private LayoutInflater _layoutInflater;
    private List<ConnectPojo> _connectPojoList;
    private TextView _descText;
    private ImageView _connectImage;

    public ConnectAdapter(Activity activity, List<ConnectPojo> connectPojoList) {
        super(activity, R.layout.connect_list_detail, connectPojoList);
        _connectPojoList = connectPojoList;
        _layoutInflater = activity.getWindow().getLayoutInflater();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View indivConnect = _layoutInflater.inflate(R.layout.connect_list_detail, parent, false);

        ConnectPojo cPojo = _connectPojoList.get(position);

        _descText = (TextView) indivConnect.findViewById(R.id.connectDescriptionText);
        _connectImage = (ImageView) indivConnect.findViewById(R.id.connectImage);

        _descText.setText(cPojo.getDescriptionText());
        _connectImage.setImageResource(cPojo.getImageViewId());

        return indivConnect;
    }
}
