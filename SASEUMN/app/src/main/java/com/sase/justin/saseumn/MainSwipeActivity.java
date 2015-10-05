package com.sase.justin.saseumn;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParsePush;

public class MainSwipeActivity extends FragmentActivity implements ActionBar.TabListener {

    private ViewPager m_viewPager;
    private ActionBar actionBar;
    private TabsAdapter m_tabsAdapter;
    private String[] m_tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_main_swipe);

        // Sets the tab layout.
        m_viewPager = (ViewPager) findViewById(R.id.pager);
        m_tabs = new String[]{"Events","News"};
        actionBar = getActionBar();

        m_tabsAdapter = new TabsAdapter(getSupportFragmentManager());
        m_viewPager.setAdapter(m_tabsAdapter);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for(String tab: m_tabs)
        {
            actionBar.addTab(actionBar.newTab()
                    .setText(tab)
                    .setTabListener(this));

        }

        m_viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

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

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        m_viewPager.setCurrentItem(tab.getPosition());
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
