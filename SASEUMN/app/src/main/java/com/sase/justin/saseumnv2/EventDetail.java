package com.sase.justin.saseumnv2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.ParsePush;


public class EventDetail extends Activity implements DetailInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        setTextFromIntent("Name", R.id.eventTitle);
        setTextFromIntent("Time", R.id.timeText);
        setHtmlTextFromIntent("Description", R.id.bodyText);
        setTextFromIntent("Location", R.id.locationText);
        setTextFromIntent("Date", R.id.dateText);

        //Special Case for FB since Links can be clickable.
        //1) Obtain the textView from XML and string from JSON
        String fbLink = getIntent().getStringExtra("Fb");
        String newFbText = "<a href='" + fbLink + "'>" + fbLink + "</a>";
        TextView fbText = (TextView) findViewById(R.id.FBText);
        fbText.setClickable(true);
        fbText.setMovementMethod(LinkMovementMethod.getInstance());
        fbText.setText(Html.fromHtml(newFbText));
    }

    public void setTextFromIntent(String key, int id) {
        String result = getIntent().getStringExtra(key);
        TextView txt = (TextView) findViewById(id);
        txt.setText(result);
    }

    public void setHtmlTextFromIntent(String key, int id) {
        String result = getIntent().getStringExtra(key);
        TextView txt = (TextView) findViewById(id);
        txt.setClickable(true);
        txt.setMovementMethod(LinkMovementMethod.getInstance());
        txt.setText(Html.fromHtml(result));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_settings:
                Intent intent = new Intent();
                intent.setClass(this, SetPreferencesActivity.class);
                startActivityForResult(intent, 0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        reloadPref();
    }

    private void reloadPref() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isPushChecked = preferences.getBoolean("push_check", false);
        if (isPushChecked) {
            ParsePush.subscribeInBackground("sase_events");
        } else {
            ParsePush.unsubscribeInBackground("sase_events");
        };
    }
}
