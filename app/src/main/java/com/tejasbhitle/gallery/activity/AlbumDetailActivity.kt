package com.tejasbhitle.gallery.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem

import androidx.appcompat.app.AppCompatActivity

import com.tejasbhitle.gallery.R
import com.tejasbhitle.gallery.fragment.MediaListFragment
import com.tejasbhitle.gallery.util.Constants

import kotlinx.android.synthetic.main.activity_album_detail.*


class AlbumDetailActivity : AppCompatActivity(){

    private val TAG = "AlbumDetailActivity"
    private val ALBUM_PATH_KEY = "ALBUM_PATH_KEY"

    private var mediaListFragment: MediaListFragment? = null
    private var albumPath: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_detail)


        if (savedInstanceState != null){
            albumPath = savedInstanceState.getString(ALBUM_PATH_KEY)
            mediaListFragment = supportFragmentManager
                    .findFragmentByTag(MediaListFragment.TAG) as MediaListFragment?

        } else{
            val bundle = intent.extras
            if (bundle != null) {
                albumPath = bundle.getString(Constants.ABS_FILE_PATH, "")
            }

            mediaListFragment = getMediaListFragment()

            supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mediaListFragment!!, MediaListFragment.TAG)
                    .commit()
        }

        setSupportActionBar(toolbar)
        supportActionBar!!.title = albumPath!!.substring(albumPath!!.lastIndexOf("/") + 1, albumPath!!.length)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


    }

    private fun getMediaListFragment(): MediaListFragment {
        val fragment = MediaListFragment()

        fragment.setMediaListFragmentListener(
                object : MediaListFragment.MediaListFragmentListener {

                    override fun getAlbumPath():String {
                        return albumPath!!
                    }

                    override fun showMediaFragment(position: Int) {
                        val intent = Intent(this@AlbumDetailActivity, MediaActivity::class.java)
                        val bundle = Bundle()
                        bundle.putString(Constants.ABS_FILE_PATH, albumPath)
                        bundle.putInt(Constants.FILE_POSITION_KEY, position)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                })
        return fragment
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(ALBUM_PATH_KEY, albumPath)
        super.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}