package com.sase.justin.saseumnv2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ConnectWithSASEActivity extends ListFragment {

    private List<ConnectPojo> _connectList;
    private ConnectAdapter _connectAdapter;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View output = inflater.inflate(R.layout.activity_connect_with_sase, container, false);

        _connectList = new ArrayList<>();
        loadConnectList();

        setAdapter();

        return output;
    }

    private void loadConnectList() {
        ConnectPojo twitterPojo = new ConnectPojo();
        ConnectPojo webPojo = new ConnectPojo();
        ConnectPojo fbPojo = new ConnectPojo();

        webPojo.setDescriptionText("SASE UMN Website");
        webPojo.setImageViewId(R.drawable.web);
        webPojo.setUrl("http://www.saseumn.org/");

        fbPojo.setDescriptionText("Like us on Facebook!");
        fbPojo.setImageViewId(R.drawable.fb);
        fbPojo.setUrl("https://www.facebook.com/sase.umn/");

        twitterPojo.setDescriptionText("Follow us on Twitter!");
        twitterPojo.setImageViewId(R.drawable.twitter);
        twitterPojo.setUrl("https://twitter.com/SASEUMN?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor");

        _connectList.add(webPojo);
        _connectList.add(fbPojo);
        _connectList.add(twitterPojo);
    }

    private void setAdapter() {
        _connectAdapter = new ConnectAdapter(getActivity(), _connectList);
        setListAdapter(_connectAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        ListAdapter adapter = getListAdapter();
        ConnectPojo cPojo = (ConnectPojo) adapter.getItem(position);

        intent.setData(Uri.parse(cPojo.getConnectUrl()));
        startActivity(intent);
    }
}
