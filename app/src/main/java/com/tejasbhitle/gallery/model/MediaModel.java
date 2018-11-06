package com.tejasbhitle.gallery.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;
import com.tejasbhitle.gallery.R;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;

public class MediaModel extends AbstractItem<MediaModel, MediaModel.ViewHolder> {

    private final static int TYPE_ID = 683881;
    private File file;

    public MediaModel(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @NonNull
    @Override
    public ViewHolder getViewHolder(View v) {
        return new ViewHolder(v);
    }

    @Override
    public int getType() {
        return TYPE_ID;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.list_item_media;
    }

    public class ViewHolder extends FastAdapter.ViewHolder<MediaModel>{

        ImageView media_image;

        public ViewHolder(View view){
            super(view);
            media_image = view.findViewById(R.id.media_image);
        }

        @Override
        public void bindView(MediaModel item, List<Object> payloads) {
            Picasso.get()
                    .load(item.file)
                    .fit()
                    .centerCrop()
                    .into(media_image);
        }

        @Override
        public void unbindView(MediaModel item) {

        }
    }

}
