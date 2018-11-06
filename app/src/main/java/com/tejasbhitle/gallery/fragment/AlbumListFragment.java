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
import com.tejasbhitle.gallery.model.AlbumModel;
import com.tejasbhitle.gallery.util.Constants;
import com.tejasbhitle.gallery.util.FileHandler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumListFragment extends Fragment
        implements SharedPreferences.OnSharedPreferenceChangeListener{


    public static final String TAG = "ALBUM_LIST_FRAGMENT";

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

        Log.e(TAG,"onCreateView");
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
        recyclerView.setLayoutManager(gridLayoutManager);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

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
        fetchImageAlbums();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        if(key.equals(getString(R.string.prefs_show_hidden))){
            fetchImageAlbums();
        }
    }

    private void fetchImageAlbums(){
        itemAdapter.clear();
        itemAdapter.add(FileHandler.getImageAlbumsUtil(
                getActivity(),
                Environment.getExternalStorageDirectory().getPath()
        ));
        recyclerView.setAdapter(fastAdapter);
    }

}
