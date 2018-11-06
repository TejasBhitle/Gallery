package com.tejasbhitle.gallery.activity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;

import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.fragment.MediaFragment;
import com.tejasbhitle.gallery.fragment.MediaListFragment;
import com.tejasbhitle.gallery.model.MediaModel;
import com.tejasbhitle.gallery.util.Constants;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AlbumDetailActivity extends AppCompatActivity {

    private static final String TAG = "AlbumDetailActivity";
    private static final String ALBUM_PATH_KEY = "ALBUM_PATH_KEY";
    private MediaListFragment mediaListFragment;
    private MediaFragment mediaFragment;
    private String albumPath = "";
    private boolean isMediaListFragmentShown = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        if(savedInstanceState != null){
            albumPath = savedInstanceState.getString(ALBUM_PATH_KEY);
            mediaListFragment = (MediaListFragment)getSupportFragmentManager()
                    .findFragmentByTag(MediaListFragment.TAG);
            mediaFragment = (MediaFragment)getSupportFragmentManager()
                    .findFragmentByTag(MediaFragment.TAG);
        }
        else{

            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                albumPath = bundle.getString(Constants.ABS_FILE_PATH,"");
            }

            mediaFragment = new MediaFragment();
            mediaFragment.setMediaFragmentListener(new MediaFragment.MediaFragmentListener() {
                @Override
                public void showMediaFragment() {
                    switchToMediaListFragment();
                }
            });

            mediaListFragment = new MediaListFragment();
            mediaListFragment.setMediaListFragmentListener(
                    new MediaListFragment.MediaListFragmentListener() {
                        @Override
                        public String getAlbumPath() {
                            return albumPath;
                        }

                        @Override
                        public void showMediaFragment(MediaModel mediaModel) {
                            switchToMediaFragment(mediaModel);
                        }
                    });

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container,mediaListFragment, MediaListFragment.TAG)
                    .commit();
        }

    }

    /*Called when an image is clicked in MediaListFragment */
    private void switchToMediaFragment(MediaModel mediaModel){
        isMediaListFragmentShown = false;
        mediaFragment.setMediaModel(mediaModel);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,mediaFragment,MediaFragment.TAG)
                .commit();
    }

    private void switchToMediaListFragment(){
        isMediaListFragmentShown = true;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,mediaListFragment,MediaListFragment.TAG)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ALBUM_PATH_KEY,albumPath);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(!isMediaListFragmentShown){
            switchToMediaListFragment();
            return;
        }
        super.onBackPressed();
    }
}
