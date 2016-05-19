package com.sase.justin.saseumnv2;

import android.preference.PreferenceFragment;
import android.os.Bundle;

public class PrefFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
