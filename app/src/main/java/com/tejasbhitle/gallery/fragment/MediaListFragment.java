package com.tejasbhitle.gallery.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.listeners.OnClickListener;
import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.model.MediaModel;
import com.tejasbhitle.gallery.util.Constants;
import com.tejasbhitle.gallery.util.FileHandler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MediaListFragment extends Fragment {

    public static final String TAG = "MEDIA_LIST_FRAGMENT";

    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private FastAdapter fastAdapter;
    private MediaListFragmentListener mediaListFragmentListener;
    private String path;
    private GridLayoutManager gridLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retain this fragment when activity is re-initialized
        setRetainInstance(true);

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
        fastAdapter.withOnClickListener(new OnClickListener<MediaModel>() {
            @Override
            public boolean onClick(View v, IAdapter<MediaModel> adapter, MediaModel item, int position) {
                mediaListFragmentListener.showMediaFragment(position);
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

        if(screenOrientation == Surface.ROTATION_90 || screenOrientation == Surface.ROTATION_270)
            ((GridLayoutManager)recyclerView.getLayoutManager()).setSpanCount(4);
        else
            ((GridLayoutManager)recyclerView.getLayoutManager()).setSpanCount(2);

        getAllMedia(path);
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
        void showMediaFragment(int position);
    }
}
