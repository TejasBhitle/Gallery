package com.tejasbhitle.gallery.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.fragment.AlbumListFragment;

public class MainActivity extends AppCompatActivity{

    private AlbumListFragment albumFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInitialFragment();

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
        }
        return true;
    }

    private void setInitialFragment(){
        albumFragment = new AlbumListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container,albumFragment)
                .commit();
    }


}
