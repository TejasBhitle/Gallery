package com.tejasbhitle.gallery.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.squareup.picasso.Picasso;
import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.model.MediaModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MediaFragment extends Fragment {

    public static final String TAG = "MEDIA_FRAGMENT";
    private GestureImageView imageView;
    private MediaModel mediaModel;
    private MediaFragmentListener mediaFragmentListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media,container,false);
        imageView = view.findViewById(R.id.media_image);

        if(mediaModel != null){
            Picasso.get()
                    .load(mediaModel.getFile())
                    .fit()
                    .centerInside()
                    .into(imageView);
        }
        return view;
    }

    public void setMediaModel(MediaModel mediaModel){
        this.mediaModel = mediaModel;
    }

    public void setMediaFragmentListener(MediaFragmentListener l){
        this.mediaFragmentListener = l;
    }

    public interface MediaFragmentListener{
        void showMediaFragment();
    }
}
