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
    private MediaListFragment mediaListFragment;
    private String albumPath = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        setInitialFragment();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            albumPath = bundle.getString(Constants.ABS_FILE_PATH,"");
        }
        Log.e(TAG,albumPath);
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

    private void setInitialFragment(){

        mediaListFragment = new MediaListFragment();
        mediaListFragment.setMediaListFragmentListener(
                new MediaListFragment.MediaListFragmentListener() {
                    @Override
                    public String getAlbumPath() {
                        return albumPath;
                    }
                });

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,mediaListFragment)
                .commit();
    }
}
