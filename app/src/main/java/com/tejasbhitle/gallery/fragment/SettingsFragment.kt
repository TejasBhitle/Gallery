package com.tejasbhitle.gallery.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.preference.*
import com.tejasbhitle.gallery.R
import com.tejasbhitle.gallery.preference.ColumnDialogPreference
import com.tejasbhitle.gallery.preference.ColumnPreferenceDialogFragmentCompat

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

    override fun onDisplayPreferenceDialog(preference: Preference) {

        Log.e(TAG,"preference not null")

        var dialogFragment: DialogFragment? = null
        if(preference is ColumnDialogPreference){
            dialogFragment = ColumnPreferenceDialogFragmentCompat
                    .newInstance(preference)
        }

        if(dialogFragment != null){
            dialogFragment.setTargetFragment(this,0)
            dialogFragment.show(this.fragmentManager,"PREFERENCE_DIALOG")
        }
        else
            super.onDisplayPreferenceDialog(preference)
    }
}