package com.tejasbhitle.gallery.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.appbar.AppBarLayout
import com.tejasbhitle.gallery.R
import com.tejasbhitle.gallery.fragment.MediaFragment
import com.tejasbhitle.gallery.util.Constants

import kotlinx.android.synthetic.main.activity_media.*

class MediaActivity : BaseActivity() {

    private val TAG = "MediaActivity"
    private lateinit var mediaFragment: MediaFragment
    private lateinit var appBarLayout: AppBarLayout
    private var isAppBarExpanded = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        appBarLayout = findViewById(R.id.appBarLayout)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mediaFragment = getMediaFragment()
        if (savedInstanceState != null) {
            mediaFragment = supportFragmentManager
                    .findFragmentByTag(MediaFragment.TAG) as MediaFragment
            mediaFragment = setMediaFragmentListener(mediaFragment)

        } else {
            callMediaFragment(intent)
        }
    }

    private fun callMediaFragment(intent: Intent) {
        val uri = intent.data
        val bundle = Bundle()
        if (uri != null) {
            Log.e(TAG, uri.toString())
            bundle.putBoolean(Constants.IS_CALLED_FROM_OUTSIDE, true)
            bundle.putParcelable(Constants.FILE_URI, uri)
        } else {
            val extras = intent.extras
            if (extras != null) {
                bundle.putBoolean(Constants.IS_CALLED_FROM_OUTSIDE, false)
                bundle.putString(Constants.ABS_FILE_PATH, extras.getString(Constants.ABS_FILE_PATH))
                bundle.putInt(Constants.FILE_POSITION_KEY, extras.getInt(Constants.FILE_POSITION_KEY))
            }
        }

        mediaFragment = getMediaFragment()
        mediaFragment.setArgs(bundle)
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, mediaFragment, MediaFragment.TAG)
                .commit()
    }

    private fun getMediaFragment(): MediaFragment {
        var fragment = MediaFragment()
        fragment = setMediaFragmentListener(fragment)
        return fragment
    }

    private fun setMediaFragmentListener(fragment: MediaFragment): MediaFragment {
        fragment.setMediaFragmentListener(
                object : MediaFragment.MediaFragmentListener {
                    override fun onMediaClick(visibility: Int) {
                        isAppBarExpanded = !isAppBarExpanded
                        appBarLayout.setExpanded(!isAppBarExpanded, true)
                    }
                })
        return fragment
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

}
