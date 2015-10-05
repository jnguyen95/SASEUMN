package com.sase.justin.saseumn;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Justin on 8/14/2015.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new EventsCalendar();
            case 1:
                return new NewsFeed();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }


}
