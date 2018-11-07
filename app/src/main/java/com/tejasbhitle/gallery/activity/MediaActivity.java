package com.tejasbhitle.gallery.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.fragment.MediaFragment;
import com.tejasbhitle.gallery.model.MediaModel;
import com.tejasbhitle.gallery.util.Constants;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MediaActivity extends AppCompatActivity {

    private static final String TAG = "MediaActivity";
    private MediaFragment mediaFragment;
    private MediaModel mediaModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        mediaFragment = getMediaFragment();
        if(savedInstanceState != null){

            mediaModel = savedInstanceState.getParcelable(Constants.MEDIA_MODEL_KEY);
            mediaFragment = (MediaFragment) getSupportFragmentManager()
                    .findFragmentByTag(MediaFragment.TAG);

        }
        else{

            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                mediaModel = bundle.getParcelable(Constants.MEDIA_MODEL_KEY);
            }
            if(mediaModel == null)
                Log.e(TAG,"mediamodel is null");
            mediaFragment = getMediaFragment();
            mediaFragment.setMediaModel(mediaModel);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container,mediaFragment, MediaFragment.TAG)
                    .commit();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.MEDIA_MODEL_KEY,mediaModel);
        super.onSaveInstanceState(outState);
    }


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
