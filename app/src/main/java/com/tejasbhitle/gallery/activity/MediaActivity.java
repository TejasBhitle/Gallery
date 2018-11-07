package com.tejasbhitle.gallery.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.fragment.MediaFragment;
import com.tejasbhitle.gallery.util.Constants;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MediaActivity extends AppCompatActivity {

    private static final String TAG = "MediaActivity";
    private MediaFragment mediaFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        mediaFragment = getMediaFragment();
        if(savedInstanceState != null){

            mediaFragment = (MediaFragment) getSupportFragmentManager()
                    .findFragmentByTag(MediaFragment.TAG);

        }
        else{

            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                mediaFragment = getMediaFragment();
                mediaFragment.setArgs(bundle);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container,mediaFragment, MediaFragment.TAG)
                        .commit();
            }
        }
    }


    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Constants.ABS_FILE_PATH, absAlbumPath);
        super.onSaveInstanceState(outState);
    }*/


    private MediaFragment getMediaFragment(){
        MediaFragment fragment = new MediaFragment();
        fragment.setMediaFragmentListener(
                new MediaFragment.MediaFragmentListener() {
                });
        return fragment;
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
