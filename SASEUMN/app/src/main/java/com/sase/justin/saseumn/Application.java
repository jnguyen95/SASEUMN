package com.sase.justin.saseumn;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParsePush;

/**
 * Created by Justin on 9/7/2015.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "WXgWSmkn6lLmqdSU0NLhQJrUSbtbNjB3IrJqogeq", "6pLmOa9URPilOkCvWiJdR6vA4063hQcFvysZB8Jw");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Sets the Push Notification Settings
        boolean isPushChecked = mySharedPreferences.getBoolean("push_check", false);
        if (isPushChecked) {
            ParsePush.subscribeInBackground("sase_events");
        }
        else
        {
            ParsePush.unsubscribeInBackground("sase_events");
        }
    }
}
