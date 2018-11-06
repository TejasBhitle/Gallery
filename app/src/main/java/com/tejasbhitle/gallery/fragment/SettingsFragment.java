package com.tejasbhitle.gallery.fragment;

import android.os.Bundle;

import com.tejasbhitle.gallery.R;

import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat{

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        addPreferencesFromResource(R.xml.settings_prefs);
    }
}
