package com.tejasbhitle.gallery.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.squareup.picasso.Picasso;
import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.adapter.MediaPagerAdapter;
import com.tejasbhitle.gallery.model.MediaModel;
import com.tejasbhitle.gallery.util.Constants;
import com.tejasbhitle.gallery.util.FileHandler;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

public class MediaFragment extends Fragment {

    public static final String TAG = "MEDIA_FRAGMENT";
    private ViewPager viewpager;
    private String absAlbumPath;
    private int position;
    private MediaFragmentListener mediaFragmentListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_media,container,false);
        viewpager = view.findViewById(R.id.viewpager);
        viewpager.setAdapter(new MediaPagerAdapter(FileHandler.getAllMedia(absAlbumPath),getContext()));
        viewpager.setCurrentItem(position);
        return view;
    }

    public void setArgs(Bundle bundle){
        this.absAlbumPath = bundle.getString(Constants.ABS_FILE_PATH);
        this.position = bundle.getInt(Constants.FILE_POSITION_KEY);
    }


    public void setMediaFragmentListener(MediaFragmentListener l){
        this.mediaFragmentListener = l;
    }

    public interface MediaFragmentListener{

    }
}
