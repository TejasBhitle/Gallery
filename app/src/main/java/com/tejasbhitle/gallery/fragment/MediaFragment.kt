package com.tejasbhitle.gallery.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tejasbhitle.gallery.R
import com.tejasbhitle.gallery.adapter.MediaPagerAdapter
import com.tejasbhitle.gallery.dialog.MediaInfoDialog
import com.tejasbhitle.gallery.model.MediaModel
import com.tejasbhitle.gallery.util.Constants
import com.tejasbhitle.gallery.util.FileHandler
import com.tejasbhitle.gallery.util.UriHandler

class MediaFragment : Fragment(){

    companion object {
        val TAG = "MEDIA_FRAGMENT"
    }
    private var viewPager: ViewPager? = null;
    private var bottomBar: BottomNavigationView? = null
    private lateinit var absAlbumPath: String
    private var position: Int? = null
    private var mediaFragmentListener: MediaFragmentListener? = null
    private var isCalledFromOutside: Boolean = false //flag to check if called from outside the app or not
    private var uri: Uri? = null
    private var mediaModels: List<MediaModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_media, container, false)

        viewPager = view.findViewById(R.id.viewpager)
        setViewpager(viewPager!!)
        bottomBar = view.findViewById(R.id.bottomNavigationView)
        setBottomBar(bottomBar!!)
        return view
    }

    private fun setViewpager(viewpager:ViewPager) {
        var fileName = ""
        if (isCalledFromOutside) {
             /*
            If called from outside, load all the media files in parent directory of
            the clicked file
            */
            val mediaPath = uri!!.path
            val slashIndex = mediaPath!!.lastIndexOf("/") + 1
            absAlbumPath = mediaPath.substring(0, slashIndex)
            fileName = mediaPath.substring(slashIndex, mediaPath.length)
        }

        (context as Activity).window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            mediaFragmentListener!!.onMediaClick(visibility)
            if ((visibility and View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                bottomBar!!.visibility = View.VISIBLE
            } else {
                bottomBar!!.visibility = View.GONE
            }
}

        mediaModels = FileHandler.getAllMedia(absAlbumPath)
        viewpager.adapter = MediaPagerAdapter(mediaModels!!, context!!,
                object : MediaPagerAdapter.PagerOnClickListener {
                    override fun onClick() {
                        Log.e(TAG, "onCLick")
                        switchFullScreen()

                    }
                })

        if (isCalledFromOutside) {
         /*Calculate the position of the clicked file to set viewpager position*/
            for (i in mediaModels!!.indices) {
                if (mediaModels!![i].file!!.name == fileName) {
                    position = i
                    break
                }
            }
        }
        viewpager.currentItem = position!!
    }

    private fun switchFullScreen() {

        val decorView = (context as Activity).window.decorView
        val flags = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        or View.SYSTEM_UI_FLAG_FULLSCREEN
        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                decorView.systemUiVisibility = flags

    }

    private fun setBottomBar(bottomBar:BottomNavigationView) {
        bottomBar.setOnNavigationItemSelectedListener(
                object:BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(menuItem: MenuItem):Boolean {
                val media = mediaModels!![viewPager!!.currentItem]
                when (menuItem.itemId) {
                    R.id.bottom_bar_item_edit -> {

                    }
                    R.id.bottom_bar_item_delete -> {

                    }
                    R.id.bottom_bar_item_share ->
                        shareMedia(media)
                    R.id.bottom_bar_item_info ->
                        MediaInfoDialog.showMediaInfoDialog(context!!, media)
                }
                return true
            }
        })
    }

    private fun shareMedia(media:MediaModel) {

        val contentUri = UriHandler.getContentUri(context!!, media)

        val shareIntent = ShareCompat.IntentBuilder.from(activity!!)
                .addStream(contentUri)
                .setType(media.mimeType)
                .intent

        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        if (shareIntent.resolveActivity(activity!!.packageManager) != null) {
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)))
        }
        else {
            Toast.makeText(context, getString(R.string.no_apps_found), Toast.LENGTH_SHORT).show()
        }
    }

    fun setArgs(bundle: Bundle) {
        this.isCalledFromOutside = bundle.getBoolean(Constants.IS_CALLED_FROM_OUTSIDE)
        if (isCalledFromOutside){
            /*Called by outside activity using intent filter*/
            this.uri = bundle.getParcelable(Constants.FILE_URI)
        }
        else {
            /*Called by activity which is called by inside activity*/
            this.absAlbumPath = bundle.getString(Constants.ABS_FILE_PATH)
            this.position = bundle.getInt(Constants.FILE_POSITION_KEY)
        }
    }

    fun setMediaFragmentListener(l:MediaFragmentListener) {
        this.mediaFragmentListener = l
    }

    interface MediaFragmentListener {
        fun onMediaClick(visibility: Int)
    }


}