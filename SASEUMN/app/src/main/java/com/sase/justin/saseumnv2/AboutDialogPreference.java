package com.sase.justin.saseumnv2;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

/**
 * Created by Justin on 9/12/2015.
 */
public class AboutDialogPreference extends DialogPreference {

    public AboutDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    // All this class does... is tell you more about SASE when you click SASE UMN in the settings.
    @Override
    protected void onPrepareDialogBuilder(Builder builder)
    {
        super.onPrepareDialogBuilder(builder);
        builder.setNegativeButton(null,null);
    }
}
