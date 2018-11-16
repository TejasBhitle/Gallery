package com.tejasbhitle.gallery.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.squareup.picasso.Picasso;
import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.util.VideoRequestHandler;

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
            media_image = view.findViewById(R.id.media_image);
            context = view.getContext();
        }

        @Override
        public void bindView(MediaModel item, List<Object> payloads) {
            if(item.isVideoFile()){
                new Picasso.Builder(context)
                        .addRequestHandler(new VideoRequestHandler())
                        .build()
                        .load(VideoRequestHandler.SCHEME_VIDEO+":"+item.getFile().getPath())
                        .fit()
                        .centerCrop()
                        .into(media_image);
            }
            else {
                Picasso.get()
                        .load(item.file)
                        .fit()
                        .centerCrop()
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
