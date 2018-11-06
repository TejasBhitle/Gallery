package com.tejasbhitle.gallery.fragment;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.util.Constants;
import com.tejasbhitle.gallery.util.FileHandler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MediaListFragment extends Fragment {

    private static final String TAG = "MediaListFragment";

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private FastAdapter fastAdapter;
    private MediaListFragmentListener mediaListFragmentListener;
    private String path;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(this.mediaListFragmentListener != null)
            path = this.mediaListFragmentListener.getAlbumPath();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_media_list,container,false);



        recyclerView = view.findViewById(R.id.recyclerview);
        itemAdapter = new ItemAdapter();
        fastAdapter = FastAdapter.with(itemAdapter);
        fastAdapter.withSelectable(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        getAllMedia(path);
        return view;
    }

    private void getAllMedia(String absAlbumPath){
        itemAdapter.clear();
        itemAdapter.add(FileHandler.getAllMedia(absAlbumPath));
        recyclerView.setAdapter(fastAdapter);
    }

    public void setMediaListFragmentListener(MediaListFragmentListener l){
        this.mediaListFragmentListener = l;
    }

    public interface MediaListFragmentListener{
        String getAlbumPath();
    }
}
