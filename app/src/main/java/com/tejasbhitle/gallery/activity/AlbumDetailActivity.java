package com.tejasbhitle.gallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.fragment.MediaListFragment;
import com.tejasbhitle.gallery.model.MediaModel;
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
            mediaListFragment = (MediaListFragment) getSupportFragmentManager()
                    .findFragmentByTag(MediaListFragment.TAG);

        }
        else{
            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                albumPath = bundle.getString(Constants.ABS_FILE_PATH,"");
            }

            mediaListFragment = getMediaListFragment();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container,mediaListFragment, MediaListFragment.TAG)
                    .commit();
        }
        setTitle(albumPath.substring(albumPath.lastIndexOf("/")+1,albumPath.length()));

    }

    private MediaListFragment getMediaListFragment(){
        MediaListFragment fragment = new MediaListFragment();
        fragment.setMediaListFragmentListener(
                new MediaListFragment.MediaListFragmentListener() {
                    @Override
                    public String getAlbumPath() {
                        return albumPath;
                    }

                    @Override
                    public void showMediaFragment(int position) {
                        Intent intent = new Intent(AlbumDetailActivity.this,MediaActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.ABS_FILE_PATH,albumPath);
                        bundle.putInt(Constants.FILE_POSITION_KEY, position);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
        return fragment;
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
        super.onBackPressed();
    }
}
