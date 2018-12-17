package com.tejasbhitle.gallery.fragment

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.preference.PreferenceManager
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.IItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.tejasbhitle.gallery.R
import com.tejasbhitle.gallery.util.FileHandler
import com.tejasbhitle.gallery.util.FileHandler.getAllMedia
import org.jetbrains.annotations.Nullable

const val BUNDLE_RECYCLER_LAYOUT = "BUNDLE_RECYCLER_LAYOUT"

class MediaListFragment : Fragment() {

    companion object {
        val TAG = "MEDIA_LIST_FRAGMENT"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter<*>
    private lateinit var fastAdapter: FastAdapter<*>
    private lateinit var mediaListFragmentListener: MediaListFragmentListener
    private lateinit var path: String
    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        path = this.mediaListFragmentListener.getAlbumPath()

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_media_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerview)
        itemAdapter = ItemAdapter<IItem<*, *>>()
        fastAdapter = FastAdapter.with<IItem<*, *>, IAdapter<out IItem<*, *>>>(itemAdapter)
        fastAdapter.withSelectable(true)
        fastAdapter.withOnClickListener{ v, adapter, item, position ->
            mediaListFragmentListener.showMediaFragment(position)
            false
        }
        val factor = PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(getString(R.string.prefs_grid_column),2)
        gridLayoutManager = GridLayoutManager(context, factor)
        getAllMedia(path)
        recyclerView.layoutManager = gridLayoutManager
        return view
    }

    override fun onResume() {
        super.onResume()

        /*
        val screenOrientation = (context!!
                .getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay
                .orientation


        if (screenOrientation == Surface.ROTATION_90 || screenOrientation == Surface.ROTATION_270)
            (recyclerView.layoutManager as GridLayoutManager).spanCount = 8
        else
            (recyclerView.layoutManager as GridLayoutManager).spanCount = 4
        */

    }

    override fun onViewStateRestored(@Nullable savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState != null) {
            val savedRecyclerLayoutState = savedInstanceState.getParcelable<Parcelable>(BUNDLE_RECYCLER_LAYOUT)
            recyclerView.layoutManager!!.onRestoreInstanceState(savedRecyclerLayoutState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT,
                recyclerView.layoutManager!!.onSaveInstanceState())
    }

    private fun getAllMedia(absAlbumPath: String) {
        itemAdapter.clear()
        itemAdapter.add(FileHandler.getAllMedia(absAlbumPath) as List<Nothing?>)
        recyclerView.adapter = fastAdapter
    }

    fun setMediaListFragmentListener(l: MediaListFragmentListener) {
        this.mediaListFragmentListener = l
    }

    interface MediaListFragmentListener {
        fun getAlbumPath(): String
        fun showMediaFragment(position: Int)
    }
}