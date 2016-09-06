package com.sase.justin.saseumnv2;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;

public class MainSwipeActivity extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager viewPager;
    private ActionBar actionBar;
    private GetDataParser dataParser;
    private Bundle fragmentBundle;
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main_swipe);

        DateParser dateParser = new DateParser();
        fragmentBundle = new Bundle();
        dataParser = new GetDataParser();

        currentDate = dateParser.getCurrentDate();

        new EventAsyncTask().execute("https://graph.facebook.com/sase.umn/events?access_token=1780762562207328|0MIw-Ju8o7dXjU5ogdgUsVHfXFM");
    }

    private class EventAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                return dataParser.GET(urls[0]);
            } catch (IOException e) {
                return "";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            if (dataParser.isValidJsonObject(result))
            {
                fragmentBundle.putString("fbEventData", result);
            }
            else
            {
                fragmentBundle.putString("fbEventData", "");
            }
            setHoloTabLayout();
        }
    }

    // TODO: Add new code for Material Design. Due to implementation change in web, this will be done later in the semester.

    // TODO: Make sure any part of the code that uses this function only uses this if (API_VERSION < 21).
    private void setHoloTabLayout()
    {
        viewPager = (ViewPager) findViewById(R.id.pager);
        String[] tabs = new String[]{"Events", "Connect"};
        //String[] tabs = new String[]{"Events", "News", "Connect"};
        actionBar = getActionBar();

        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), fragmentBundle);
        viewPager.setAdapter(tabsAdapter);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for(String tab: tabs)
        {
            actionBar.addTab(actionBar.newTab()
                    .setText(tab)
                    .setTabListener(this));

        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_swipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this, SetPreferencesActivity.class);
            startActivityForResult(intent, 0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Functions below correspond to versions less than Android 5.0.
       TODO: Make sure that we separate these.
     */
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
