package com.tejasbhitle.gallery.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.activity.AlbumDetailActivity;
import com.tejasbhitle.gallery.enums.Sort;
import com.tejasbhitle.gallery.model.AlbumModel;
import com.tejasbhitle.gallery.util.Constants;
import com.tejasbhitle.gallery.util.FileHandler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumListFragment extends Fragment
        implements SharedPreferences.OnSharedPreferenceChangeListener{


    public static final String TAG = "ALBUM_LIST_FRAGMENT";
    private static final String BUNDLE_RECYCLERVIEW_LAYOUT = "BUNDLE_RECYCLERVIEW_LAYOUT";

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private FastAdapter fastAdapter;
    private GridLayoutManager gridLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retain this fragment when activity is re-initialized
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_album_view,container,false);

        recyclerView = view.findViewById(R.id.recyclerview);
        itemAdapter = new ItemAdapter();
        fastAdapter = FastAdapter.with(itemAdapter);
        fastAdapter.withSelectable(true);
        fastAdapter.withOnClickListener(new OnClickListener<AlbumModel>() {
            @Override
            public boolean onClick(View v, IAdapter<AlbumModel> adapter, AlbumModel item, int position) {
                Intent intent = new Intent(getActivity(),AlbumDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.ABS_FILE_PATH,item.getFilePath());
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                return false;
            }
        });
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        fetchImageAlbums();
        recyclerView.setLayoutManager(gridLayoutManager);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);


        int screenOrientation = ((WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay()
                .getOrientation();

        if(screenOrientation == Surface.ROTATION_90 || screenOrientation == Surface.ROTATION_270){
            ((GridLayoutManager)recyclerView.getLayoutManager()).setSpanCount(4);

        }
        else{
            ((GridLayoutManager)recyclerView.getLayoutManager()).setSpanCount(2);

        }
    }

    @Override
    public void onPause() {
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLERVIEW_LAYOUT);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLERVIEW_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        Log.e(TAG,"onSharedPreferenceChanged");
        fetchImageAlbums();
        
    }

    private Sort getSort(){
        String sort = PreferenceManager.getDefaultSharedPreferences(getContext())
                .getString(Constants.PREF_ALBUM_SORT,Sort.NAME_ASCEND.toString());
        return Sort.getSort(sort);
    }

    private void fetchImageAlbums(){
        itemAdapter.clear();
        itemAdapter.add(FileHandler.getImageAlbumsUtil(
                getActivity(),
                Environment.getExternalStorageDirectory().getPath(),
                getSort()
        ));
        recyclerView.setAdapter(fastAdapter);
    }

}
