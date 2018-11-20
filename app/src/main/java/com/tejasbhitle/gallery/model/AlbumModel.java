package com.tejasbhitle.gallery.model;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.util.FileHandler;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;

public class AlbumModel extends AbstractItem<AlbumModel, AlbumModel.ViewHolder> {

    private static final String TAG = "AlbumModel";
    private final static int TYPE_ID = 4838481;
    private File file;
    private int numOfFiles;

    public AlbumModel(File file, int numOfFiles) {
        this.file = file;
        this.numOfFiles = numOfFiles;

    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return this.file.getName();
    }

    public String getFilePath() {
        return this.file.getAbsolutePath();
    }

    public int getNumOfFiles() {
        return numOfFiles;
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
        return R.layout.list_item_album;
    }

    public static class ViewHolder extends FastAdapter.ViewHolder<AlbumModel> {


        TextView album_name,album_size;//,album_path;
        ImageView album_image;
        Context context;

        ViewHolder(View view){
            super(view);
            album_name = view.findViewById(R.id.album_name);
            //album_path = view.findViewById(R.id.album_path);
            album_size = view.findViewById(R.id.album_size);
            album_image = view.findViewById(R.id.album_image);
            context = view.getContext();
        }

        @Override
        public void bindView(AlbumModel item, List<Object> payloads) {
            album_name.setText(item.getName());
            //album_path.setText(item.getFilePath().replace(Constants.INTERNAL_STORAGE_PATH,"/"));
            album_size.setText("("+String.valueOf(item.getNumOfFiles())+")");

            MediaModel media = new MediaModel(FileHandler.getThumbnail(item.getFile()));
            if(media.isVideoFile()){
                Glide.with(context)
                        .load(media.getFile())
                        .apply(new RequestOptions().fitCenter().centerCrop())
                        .thumbnail(0.1f)
                        .into(album_image);
            }
            else {
                Glide.with(context)
                        .load(media.getFile())
                        .apply(new RequestOptions().fitCenter().centerCrop())
                        .into(album_image);

            }
        }

        @Override
        public void unbindView(AlbumModel item) {
        }
    }
}
