package com.tejasbhitle.gallery.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.fragment.MediaFragment;
import com.tejasbhitle.gallery.util.Constants;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MediaActivity extends AppCompatActivity {

    private static final String TAG = "MediaActivity";
    private MediaFragment mediaFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mediaFragment = getMediaFragment();
        if(savedInstanceState != null){

            mediaFragment = (MediaFragment) getSupportFragmentManager()
                    .findFragmentByTag(MediaFragment.TAG);

        }
        else{
            callMediaFragment(getIntent());
        }
    }

    private void callMediaFragment(Intent intent){
        Uri uri = intent.getData();
        Bundle bundle = new Bundle();
        if(uri != null){
            Log.e(TAG,uri.toString());
            bundle.putBoolean(Constants.IS_CALLED_FROM_OUTSIDE,true);
            bundle.putParcelable(Constants.FILE_URI,uri);
        }
        else{
            Bundle extras = intent.getExtras();
            if(extras != null) {
                bundle.putBoolean(Constants.IS_CALLED_FROM_OUTSIDE,false);
                bundle.putString(Constants.ABS_FILE_PATH, extras.getString(Constants.ABS_FILE_PATH));
                bundle.putInt(Constants.FILE_POSITION_KEY, extras.getInt(Constants.FILE_POSITION_KEY));
            }
        }

        mediaFragment = getMediaFragment();
        mediaFragment.setArgs(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,mediaFragment, MediaFragment.TAG)
                .commit();
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
