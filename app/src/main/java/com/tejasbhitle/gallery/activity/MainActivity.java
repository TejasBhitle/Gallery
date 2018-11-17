package com.tejasbhitle.gallery.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.enums.Sort;
import com.tejasbhitle.gallery.fragment.AlbumListFragment;
import com.tejasbhitle.gallery.util.Constants;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 6484;
    private AlbumListFragment albumFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setTitle(getString(R.string.app_name));

        if(!isWriteStoragePermissionGranted()){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_REQUEST_CODE);
            return;
        }

        if(savedInstanceState != null) {
            albumFragment = (AlbumListFragment) getSupportFragmentManager()
                    .findFragmentByTag(AlbumListFragment.TAG);

        }
        else {
            albumFragment = new AlbumListFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container,albumFragment, AlbumListFragment.TAG)
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_settings:
                startActivity(new Intent(MainActivity.this,SettingsActivity.class));
                break;
            case R.id.menu_item_sort_album_by_name:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putString(Constants.PREF_ALBUM_SORT, Sort.NAME_ASCEND.toString())
                        .apply();
                break;
            case R.id.menu_item_sort_album_by_date:
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putString(Constants.PREF_ALBUM_SORT, Sort.DATE_ASCEND.toString())
                        .apply();
                break;
        }
        return true;
    }

    private boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            return (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //resume tasks needing this permission
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
                else{
                    Toast.makeText(this,R.string.storage_permission_needed, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


}
