package com.tejasbhitle.gallery.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private FastAdapter fastAdapter;

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
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
