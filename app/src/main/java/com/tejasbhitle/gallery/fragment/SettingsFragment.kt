package com.tejasbhitle.gallery.fragment

import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.tejasbhitle.gallery.R

class SettingsFragment : PreferenceFragmentCompat() {

    private val TAG = "SettingsFragment"

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        addPreferencesFromResource(R.xml.settings_prefs)
        bindPreferenceSummaryToValue(findPreference(getString(R.string.prefs_theme)))
    }

    private fun bindPreferenceSummaryToValue(preference: Preference) {
        preference.onPreferenceChangeListener = bindPreferenceSummaryToValueListener

        bindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.context)
                        .getString(preference.key, ""))
    }

    private val bindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener {
        preference, newValue ->
        val stringValue = newValue.toString()

        if (preference is ListPreference) {
            val index = preference.findIndexOfValue(stringValue)

            preference.setSummary(
                    if (index >= 0) preference.entries[index]
                    else null
            )
        }
        true
    }
}