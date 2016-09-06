package com.sase.justin.saseumnv2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class NewsDetail extends Activity implements DetailInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        /* This contains the image library needed should there be images in the app. Right now...
        Progress for images on the app has been stalled and there is no intention of adding
        images. */

//        DisplayImageOptions defOptions = new DisplayImageOptions.Builder()
//                .build();
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
//                .defaultDisplayImageOptions(defOptions)
//                .build();
//        ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.init(config);

        setTextFromIntent("Title", R.id.newsTitle);
        setHtmlTextFromIntent("Content", R.id.contentText);
        setTextFromIntent("Date", R.id.dateText);

//        String imageUrl = getIntent().getStringExtra("ImageUrl");
//
//        //TEST CODE, THIS MAY BREAK!
//        ImageView img = (ImageView) findViewById(R.id.newsImage);
//        imageLoader.displayImage(imageUrl, img);

    }

    public void setTextFromIntent(String key, int id) {
        String result = getIntent().getStringExtra(key);
        TextView txt = (TextView) findViewById(id);
        txt.setText(result);
    }

    public void setHtmlTextFromIntent(String key, int id) {
        String result = getIntent().getStringExtra(key);
        TextView txt = (TextView) findViewById(id);
        txt.setMovementMethod(LinkMovementMethod.getInstance());
        txt.setText(Html.fromHtml(result));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_detail, menu);
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
    }

    /* @Override
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
    } */
}
