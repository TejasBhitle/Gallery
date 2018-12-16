package com.tejasbhitle.gallery.fragment

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.view.*

import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.adapters.ItemAdapter

import com.tejasbhitle.gallery.R
import com.tejasbhitle.gallery.activity.AlbumDetailActivity
import com.tejasbhitle.gallery.enums.Sort
import com.tejasbhitle.gallery.model.AlbumModel
import com.tejasbhitle.gallery.util.Constants
import com.tejasbhitle.gallery.util.FileHandler

class AlbumListFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener{

    companion object {
        val TAG = "ALBUM_LIST_FRAGMENT"
    }

    private val BUNDLE_RECYCLERVIEW_LAYOUT = "BUNDLE_RECYCLERVIEW_LAYOUT"
    private lateinit var recyclerView : RecyclerView
    private lateinit var itemAdapter: ItemAdapter<*>
    private lateinit var fastAdapter: FastAdapter<*>
    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // retain this fragment when activity is re-initialized
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_album_view, container, false)
        recyclerView = view.findViewById(R.id.recyclerview)
        itemAdapter = ItemAdapter<IItem<*, *>>()
        fastAdapter = FastAdapter.with<IItem<*, *>, IAdapter<out IItem<*, *>>>(itemAdapter)
        fastAdapter.withSelectable(true)

        fastAdapter.withOnClickListener{ v, adapter, item, position ->
            val intent = Intent(activity, AlbumDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putString(Constants.ABS_FILE_PATH, (item as AlbumModel).filePath)
            intent.putExtras(bundle)
            activity!!.startActivity(intent)
            false
        }

        gridLayoutManager = GridLayoutManager(context, 2)
        fetchImageAlbums()
        recyclerView.layoutManager = gridLayoutManager
        return view
    }

    override fun onResume() {
        super.onResume()
        PreferenceManager.getDefaultSharedPreferences(context!!)
                .registerOnSharedPreferenceChangeListener(this)

        val screenOrientation = (context!!
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay
                .orientation

        if (screenOrientation == Surface.ROTATION_90 || screenOrientation == Surface.ROTATION_270)
            (recyclerView.layoutManager as GridLayoutManager).spanCount = 4
        else
            (recyclerView.layoutManager as GridLayoutManager).spanCount = 2
    }

    override fun onPause() {
        PreferenceManager.getDefaultSharedPreferences(context!!)
                .unregisterOnSharedPreferenceChangeListener(this)
        super.onPause()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            val savedRecyclerLayoutState = savedInstanceState.getParcelable<Parcelable>(BUNDLE_RECYCLERVIEW_LAYOUT)
            recyclerView.layoutManager!!.onRestoreInstanceState(savedRecyclerLayoutState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(BUNDLE_RECYCLERVIEW_LAYOUT, recyclerView!!.layoutManager!!.onSaveInstanceState())
    }


    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        fetchImageAlbums()
    }

    private fun getSort(): Sort {
        val sort = PreferenceManager.getDefaultSharedPreferences(context!!)
                .getString(Constants.PREF_ALBUM_SORT, Sort.NAME_ASCEND.toString())
        return Sort.getSort(sort!!)
    }

    private fun fetchImageAlbums() {
        itemAdapter.clear()
        itemAdapter.add(FileHandler.getImageAlbumsUtil(
                activity!!,
                Environment.getExternalStorageDirectory().path,
                getSort()
        ) as List<Nothing?>)
        recyclerView.adapter = fastAdapter
    }

}