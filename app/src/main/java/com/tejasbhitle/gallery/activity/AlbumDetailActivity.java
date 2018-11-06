package com.tejasbhitle.gallery.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.fragment.MediaListFragment;
import com.tejasbhitle.gallery.util.Constants;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AlbumDetailActivity extends AppCompatActivity {

    private static final String TAG = "AlbumDetailActivity";
    private static final String ALBUM_PATH_KEY = "ALBUM_PATH_KEY";
    private MediaListFragment mediaListFragment;
    private String albumPath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);


        if(savedInstanceState != null){
            albumPath = savedInstanceState.getString(ALBUM_PATH_KEY);
            mediaListFragment = (MediaListFragment)getSupportFragmentManager()
                    .findFragmentByTag(MediaListFragment.TAG);
        }
        else{

            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                albumPath = bundle.getString(Constants.ABS_FILE_PATH,"");
            }

            mediaListFragment = new MediaListFragment();
            mediaListFragment.setMediaListFragmentListener(
                    new MediaListFragment.MediaListFragmentListener() {
                        @Override
                        public String getAlbumPath() {
                            return albumPath;
                        }
                    });

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container,mediaListFragment,MediaListFragment.TAG)
                    .commit();
        }

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

}
