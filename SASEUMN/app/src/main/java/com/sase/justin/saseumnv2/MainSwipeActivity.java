package com.sase.justin.saseumnv2;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParsePush;

public class MainSwipeActivity extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager _viewPager;
    private ActionBar _actionBar;
    private TabsAdapter _tabsAdapter;
    private String[] _tabs;
    private DateOperations _dateParser;
    private BGOperations _bgParser;
    private Bundle _fragmentBundle;

    private String _currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main_swipe);

        _dateParser = new DateOperations();
        _fragmentBundle = new Bundle();
        _bgParser = new BGOperations();

        _currentDate = _dateParser.getCurrentDate();
        new EventAsyncTask().execute("http://www.saseumn.org/API/event.php?msg=query&byDate=" + _currentDate + "&timeframe=future");
    }

    private class EventAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return BGOperations.GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            if (_bgParser.isValidJsonArray(result))
            {
                _fragmentBundle.putString("eventData", result);
            }
            else
            {
                _fragmentBundle.putString("eventData", "");
            }
            new NewsAsyncTask().execute("http://www.saseumn.org/API/news.php?msg=query&byDate=" + _currentDate + "&timeframe=past");
        }
    }

    private class NewsAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return BGOperations.GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {
            if (_bgParser.isValidJsonArray(result))
            {
                _fragmentBundle.putString("newsData", result);
            }
            else
            {
                _fragmentBundle.putString("newsData", "");
            }

            setTabLayout();
        }
    }

    // TODO: Make sure any part of the code that uses this function only uses this if (API_VERSION < 21).
    private void setTabLayout()
    {
        // Sets the tab layout.
        _viewPager = (ViewPager) findViewById(R.id.pager);
        _tabs = new String[]{"Events","News","Connect"};
        _actionBar = getActionBar();

        _tabsAdapter = new TabsAdapter(getSupportFragmentManager(), _fragmentBundle);
        _viewPager.setAdapter(_tabsAdapter);

        _actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for(String tab: _tabs)
        {
            _actionBar.addTab(_actionBar.newTab()
                    .setText(tab)
                    .setTabListener(this));

        }

        _viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                _actionBar.setSelectedNavigationItem(position);
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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        _viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        reloadPref();
    }

    private void reloadPref()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isPushChecked = preferences.getBoolean("push_check", false);
        if (isPushChecked) {
            ParsePush.subscribeInBackground("sase_events");
        } else {
            ParsePush.unsubscribeInBackground("sase_events");
        };
    }
}
