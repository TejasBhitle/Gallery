package com.tejasbhitle.gallery.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager

import com.tejasbhitle.gallery.R
import com.tejasbhitle.gallery.enums.Sort
import com.tejasbhitle.gallery.fragment.AlbumListFragment
import com.tejasbhitle.gallery.util.Constants
import com.tejasbhitle.gallery.util.ThemeManager

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){

    private val TAG = "MainActivity"
    private val STORAGE_PERMISSION_REQUEST_CODE = 6484
    private lateinit var albumFragment: AlbumListFragment
    internal var theme: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        theme = ThemeManager.getTheme(this)
        setTheme(theme)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = getString(R.string.app_name)

        if (!isWriteStoragePermissionGranted()) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_REQUEST_CODE)
            return
        }

        if (savedInstanceState != null) {
            albumFragment = supportFragmentManager
                    .findFragmentByTag(AlbumListFragment.TAG) as AlbumListFragment

        } else {
            albumFragment = AlbumListFragment()
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, albumFragment!!, AlbumListFragment.TAG)
                    .commit()
        }
    }

    override fun onResume() {
        if (isThemeChanged()) {
            recreate()
        }
        super.onResume()
    }

    private fun isThemeChanged(): Boolean {
        return theme != ThemeManager.getTheme(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu_item_settings ->
                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))

            R.id.menu_item_sort_album_by_name ->
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putString(Constants.PREF_ALBUM_SORT, Sort.NAME_ASCEND.toString())
                        .apply()

            R.id.menu_item_sort_album_by_date ->
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putString(Constants.PREF_ALBUM_SORT, Sort.DATE_ASCEND.toString())
                        .apply()
        }
        return true
    }

    private fun isWriteStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            STORAGE_PERMISSION_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //resume tasks needing this permission
                    val intent = intent
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this, R.string.storage_permission_needed, Toast.LENGTH_SHORT).show()
                }
        }
    }


}