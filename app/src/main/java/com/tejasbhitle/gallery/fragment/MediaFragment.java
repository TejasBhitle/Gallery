package com.tejasbhitle.gallery.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.adapter.MediaPagerAdapter;
import com.tejasbhitle.gallery.model.MediaModel;
import com.tejasbhitle.gallery.util.Constants;
import com.tejasbhitle.gallery.util.FileHandler;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class MediaFragment extends Fragment {

    public static final String TAG = "MEDIA_FRAGMENT";
    private ViewPager viewpager;
    private BottomNavigationView bottomBar;
    private String absAlbumPath;
    private Integer position;
    private MediaFragmentListener mediaFragmentListener;
    private boolean IS_CALLED_FROM_OUTSIDE; //flag to check if called from outside the app or not
    private Uri uri;
    private List<MediaModel> mediaModels;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_media,container,false);
        viewpager = view.findViewById(R.id.viewpager);
        setViewpager(viewpager);

        bottomBar = view.findViewById(R.id.bottomNavigationView);
        setBottomBar(bottomBar);
        return view;
    }

    private void setViewpager(ViewPager viewpager){
        String fileName = "";
        if(IS_CALLED_FROM_OUTSIDE){
            /*
            If called from outside, load all the media files in parent directory of
            the clicked file
            */
            String mediaPath = uri.getPath();
            int slash_index = mediaPath.lastIndexOf("/") + 1;
            absAlbumPath = mediaPath.substring(0,slash_index);
            fileName = mediaPath.substring(slash_index, mediaPath.length());
        }

        mediaModels = FileHandler.getAllMedia(absAlbumPath);
        viewpager.setAdapter(new MediaPagerAdapter(mediaModels, getContext(),
                new MediaPagerAdapter.PagerOnClickListener() {
                    @Override
                    public void onClick() {
                        mediaFragmentListener.onMediaClick();
                        if(bottomBar.isShown()){
                            bottomBar.setVisibility(View.GONE);
                        }
                        else
                            bottomBar.setVisibility(View.VISIBLE);
                    }
                }));

        if(IS_CALLED_FROM_OUTSIDE){
            /*Calculate the position of the clicked file to set viewpager position*/
            for(int i=0;i<mediaModels.size();i++){
                if(mediaModels.get(i).getFile().getName().equals(fileName)) {
                    position = i;
                    break;
                }
            }
        }
        viewpager.setCurrentItem(position);
    }

    private void setBottomBar(BottomNavigationView bottomBar){
        bottomBar.setVisibility(View.GONE);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.bottom_bar_item_copy:
                        break;
                    case R.id.bottom_bar_item_delete:
                        break;
                    case R.id.bottom_bar_item_share:
                        break;
                    case R.id.bottom_bar_item_info:
                        break;
                }
                return true;
            }
        });
    }


    public void setArgs(Bundle bundle){
        this.IS_CALLED_FROM_OUTSIDE = bundle.getBoolean(Constants.IS_CALLED_FROM_OUTSIDE);
        if(IS_CALLED_FROM_OUTSIDE){
            /*Called by outside activity using intent filter*/
            this.uri = bundle.getParcelable(Constants.FILE_URI);
        }
        else{
            /*Called by activity which is called by inside activity*/
            this.absAlbumPath = bundle.getString(Constants.ABS_FILE_PATH);
            this.position = bundle.getInt(Constants.FILE_POSITION_KEY);
        }
    }


    public void setMediaFragmentListener(MediaFragmentListener l){
        this.mediaFragmentListener = l;
    }

    public interface MediaFragmentListener{
        void onMediaClick();
    }
}
