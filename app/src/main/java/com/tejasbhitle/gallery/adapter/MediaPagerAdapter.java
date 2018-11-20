package com.tejasbhitle.gallery.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexvasilkov.gestures.Settings;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.Glide;
import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.model.MediaModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class MediaPagerAdapter extends PagerAdapter {

    List<MediaModel> mediaModels;
    Context context;
    PagerOnClickListener pagerOnClickListener;


    public MediaPagerAdapter(List<MediaModel> mediaModels,Context context, PagerOnClickListener listener){
        this.mediaModels = mediaModels;
        this.context = context;
        this.pagerOnClickListener = listener;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final MediaModel mediaModel = mediaModels.get(position);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.pager_media_model,container,false);

        GestureImageView imageView = view.findViewById(R.id.media_image);

        if(mediaModel.isVideoFile()){

            Glide.with(context)
                    .load(mediaModel.getFile())
                    .thumbnail(0.1f)
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

            Glide.with(context)
                    .load(mediaModel.getFile())
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

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pagerOnClickListener != null && !mediaModel.isVideoFile()){
                        pagerOnClickListener.onClick();
                    }
                }
            });
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

    public interface PagerOnClickListener{
        public void onClick();
    }
}
