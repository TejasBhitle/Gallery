package com.tejasbhitle.gallery.model;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.tejasbhitle.gallery.R;

import java.io.File;
import java.net.URLConnection;
import java.util.List;

import androidx.annotation.NonNull;

public class MediaModel extends AbstractItem<MediaModel, MediaModel.ViewHolder>
        implements Parcelable {

    private final static int TYPE_ID = 683881;
    private static final String TAG = "MediaModel";
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

    public boolean isVideoFile() {
        try {

            String mimeType = URLConnection.guessContentTypeFromName(this.file.getPath());
            return mimeType != null && mimeType.startsWith("video");
        }catch (StringIndexOutOfBoundsException e){
            Log.e(TAG,file.getPath());
            e.printStackTrace();
        }
        return false;
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
        Context context;

        public ViewHolder(View view){
            super(view);
            context = view.getContext();
            media_image = view.findViewById(R.id.media_image);


            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            int width = displayMetrics.widthPixels;
            int factor = 4;
            media_image.getLayoutParams().height = width/factor;

        }

        @Override
        public void bindView(MediaModel item, List<Object> payloads) {
            if(item.isVideoFile()){

                Glide.with(context)
                        .load(item.file)
                        .apply(new RequestOptions().fitCenter().centerCrop())
                        .thumbnail(0.1f)
                        .into(media_image);
            }
            else {
                Glide.with(context)
                        .load(item.file)
                        .apply(new RequestOptions().fitCenter().centerCrop())
                        .into(media_image);
            }
        }

        @Override
        public void unbindView(MediaModel item) {

        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.file);
    }

    protected MediaModel(Parcel in) {
        this.file = (File) in.readSerializable();
    }

    public static final Parcelable.Creator<MediaModel> CREATOR = new Parcelable.Creator<MediaModel>() {
        @Override
        public MediaModel createFromParcel(Parcel source) {
            return new MediaModel(source);
        }

        @Override
        public MediaModel[] newArray(int size) {
            return new MediaModel[size];
        }
    };
}
