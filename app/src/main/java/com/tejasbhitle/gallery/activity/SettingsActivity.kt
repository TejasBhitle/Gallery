package com.tejasbhitle.gallery.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem

import com.tejasbhitle.gallery.R
import androidx.preference.PreferenceManager
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.settings)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()

    }

    override fun onSharedPreferenceChanged(preference: SharedPreferences, key: String) {
        if (key == getString(R.string.prefs_theme)) {
            recreate()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}
