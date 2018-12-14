package com.tejasbhitle.gallery.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.adapter.MediaPagerAdapter;
import com.tejasbhitle.gallery.dialog.MediaInfoDialog;
import com.tejasbhitle.gallery.model.MediaModel;
import com.tejasbhitle.gallery.util.Constants;
import com.tejasbhitle.gallery.util.FileHandler;
import com.tejasbhitle.gallery.util.UriHandler;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
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

        ((Activity)getContext()).getWindow().getDecorView()
                .setOnSystemUiVisibilityChangeListener(
                        new View.OnSystemUiVisibilityChangeListener() {
                            @Override
                            public void onSystemUiVisibilityChange(int visibility) {
                                mediaFragmentListener.onMediaClick(visibility);
                                if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                    bottomBar.setVisibility(View.VISIBLE);
                                }
                                else{
                                    bottomBar.setVisibility(View.GONE);
                                }
                            }
                        });

        mediaModels = FileHandler.getAllMedia(absAlbumPath);
        viewpager.setAdapter(new MediaPagerAdapter(mediaModels, getContext(),
                new MediaPagerAdapter.PagerOnClickListener() {
                    @Override
                    public void onClick() {
                        Log.e(TAG,"onCLick");
                        switchFullScreen();

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

    private void switchFullScreen(){

        View decorView = ((Activity)getContext()).getWindow().getDecorView();
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(flags);

    }

    private void setBottomBar(BottomNavigationView bottomBar){
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                MediaModel media = mediaModels.get(viewpager.getCurrentItem());
                switch (menuItem.getItemId()){
                    case R.id.bottom_bar_item_edit:
                        break;
                    case R.id.bottom_bar_item_delete:
                        break;
                    case R.id.bottom_bar_item_share:
                        shareMedia(media);
                        break;
                    case R.id.bottom_bar_item_info:
                        MediaInfoDialog.showMediaInfoDialog(getContext(),media);
                        break;
                }
                return true;
            }
        });
    }

    private void shareMedia(MediaModel media){

        Uri contentUri = UriHandler.getContentUri(getContext(), media);

        Intent shareIntent = ShareCompat.IntentBuilder.from(getActivity())
                .addStream(contentUri)
                .setType(media.getMimeType())
                .getIntent();


        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (shareIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));
        }
        else{
            Toast.makeText(getContext(),getString(R.string.no_apps_found),Toast.LENGTH_SHORT).show();
        }
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
        void onMediaClick(int visibility);
    }
}
