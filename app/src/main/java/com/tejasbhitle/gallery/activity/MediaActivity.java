package com.tejasbhitle.gallery.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.AppBarLayout;
import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.fragment.MediaFragment;
import com.tejasbhitle.gallery.util.Constants;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MediaActivity extends AppCompatActivity {

    private static final String TAG = "MediaActivity";
    private MediaFragment mediaFragment;
    private AppBarLayout appBarLayout;
    boolean isExpanded = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        appBarLayout = findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                isExpanded = (verticalOffset == 0);
            }
        });

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mediaFragment = getMediaFragment();
        if(savedInstanceState != null){

            mediaFragment = (MediaFragment) getSupportFragmentManager()
                    .findFragmentByTag(MediaFragment.TAG);
            mediaFragment = setMediaFragmentListener(mediaFragment);

        }
        else{
            callMediaFragment(getIntent());
        }
        switchFullScreen();//switch to full screen initially
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

    private void switchFullScreen(){
        appBarLayout.setExpanded(!isExpanded,true);

        View decorView = getWindow().getDecorView();
        if(isExpanded) {
            /* Make it full screen */
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        else {
            /* Switch to normal */
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

    }

    private MediaFragment getMediaFragment(){
        MediaFragment fragment = new MediaFragment();
        fragment = setMediaFragmentListener(fragment);
        return fragment;
    }

    private MediaFragment setMediaFragmentListener(MediaFragment fragment){
        if(fragment == null) return null;
        fragment.setMediaFragmentListener(
                new MediaFragment.MediaFragmentListener() {
                    @Override
                    public void onMediaClick() {
                        switchFullScreen();
                    }
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
