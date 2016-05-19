package com.sase.justin.saseumnv2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Justin on 8/14/2015.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private Bundle _dataBundle;

    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    public TabsAdapter(FragmentManager fm, Bundle inputBundle) {
        super(fm);
        _dataBundle = inputBundle;
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                EventsCalendar evCalFragment = new EventsCalendar();
                evCalFragment.setArguments(_dataBundle);
                return evCalFragment;
            case 1:
                NewsFeed newsFeedFragment = new NewsFeed();
                newsFeedFragment.setArguments(_dataBundle);
                return newsFeedFragment;
            case 2:
                ConnectWithSASEActivity connectFragment = new ConnectWithSASEActivity();
                return connectFragment;
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }


}
