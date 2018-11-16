package com.tejasbhitle.gallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.squareup.picasso.Picasso;
import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.model.MediaModel;
import com.tejasbhitle.gallery.util.VideoRequestHandler;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class MediaPagerAdapter extends PagerAdapter {

    List<MediaModel> mediaModels;
    Context context;
    Picasso picassoInstance;


    public MediaPagerAdapter(List<MediaModel> mediaModels,Context context){
        this.mediaModels = mediaModels;
        this.context = context;
        this.picassoInstance = new Picasso.Builder(context)
                .addRequestHandler(new VideoRequestHandler())
                .build();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final MediaModel mediaModel = mediaModels.get(position);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.pager_media_model,container,false);

        GestureImageView imageView = view.findViewById(R.id.media_image);

        if(mediaModel.isVideoFile()){
            this.picassoInstance
                    .load(VideoRequestHandler.SCHEME_VIDEO+":"+mediaModel.getFile().getPath())
                    .fit()
                    .centerInside()
                    .into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(mediaModel.getFile().getPath()),"video/*");
                    context.startActivity(intent);
                }
            });

        }else {

            Picasso.get()
                    .load(mediaModel.getFile())
                    .fit()
                    .centerInside()
                    .into(imageView);

            imageView.getController().getSettings()
                    .setMaxZoom(4f)
                    .setDoubleTapZoom(-1f) // Falls back to max zoom level
                    .setPanEnabled(true)
                    .setZoomEnabled(true)
                    .setDoubleTapEnabled(true)
                    .setRotationEnabled(true)
                    .setRestrictRotation(true)
                    .setOverscrollDistance(0f, 0f)
                    .setOverzoomFactor(2f)
                    .setFillViewport(false)
                    .setFitMethod(Settings.Fit.INSIDE)
                    .setGravity(Gravity.CENTER);
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mediaModels.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
