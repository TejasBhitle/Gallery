package com.tejasbhitle.gallery.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.tejasbhitle.gallery.BuildConfig;
import com.tejasbhitle.gallery.enums.Sort;
import com.tejasbhitle.gallery.model.AlbumModel;
import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.model.MediaModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import androidx.core.content.FileProvider;
import androidx.preference.PreferenceManager;

public class FileHandler {

    private static final String TAG = "FileHandler";
    private static boolean showHidden = false;

    @TargetApi(24)
    public static List<AlbumModel> getImageAlbumsUtil(Context context, String path, Sort albumSortBy){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        showHidden = prefs.getBoolean(
                context.getString(R.string.prefs_show_hidden),
                false
        );
        List<AlbumModel> albums = getImageAlbums(path);
        Comparator<AlbumModel> comparator = null;
        switch (albumSortBy){
            case NAME_ASCEND:
                comparator = new Comparator<AlbumModel>() {
                    @Override
                    public int compare(AlbumModel o1, AlbumModel o2) {
                        int result = o1.getName().compareTo(o2.getName());
                        if(result == 0) return 0;
                        return result/Math.abs(result);
                    }
                };
                break;
            case NAME_DESCEND:
                comparator = new Comparator<AlbumModel>() {
                    @Override
                    public int compare(AlbumModel o1, AlbumModel o2) {
                        int result = o1.getName().compareTo(o2.getName());
                        if(result == 0) return 0;
                        return -(result/Math.abs(result));
                    }
                };
                break;
            case DATE_ASCEND:
                comparator = new Comparator<AlbumModel>() {
                    @Override
                    public int compare(AlbumModel o1, AlbumModel o2) {
                        return Math.toIntExact(o1.getFile().lastModified() - o2.getFile().lastModified());
                    }
                };
                break;
            case DATE_DESCEND:
                comparator = new Comparator<AlbumModel>() {
                    @Override
                    public int compare(AlbumModel o1, AlbumModel o2) {
                        return -(Math.toIntExact(o1.getFile().lastModified() - o2.getFile().lastModified()));
                    }
                };
                break;

        }
        albums.sort(comparator);
        return albums;
    }

    private static List<AlbumModel> getImageAlbums(String path){
        List<AlbumModel> albums = new ArrayList<>();
        File BASEDIR = new File(path);

        File[] files = BASEDIR.listFiles();
        for(File file : files){
            if (isAlbumValid(file)){
                File[] imagesFiles = file.listFiles(new MediaFileFilter());
                if(imagesFiles.length > 0){
                    albums.add(new AlbumModel(file, imagesFiles.length));
                }
                albums.addAll(getImageAlbums(path+'/'+file.getName()));
            }
        }
        return albums;
    }

    private static boolean isAlbumValid(File file){
        if(!file.isDirectory()) return false;
        if(!showHidden)
            if(file.getName().startsWith("."))
                return false;
        if(file.getAbsolutePath().startsWith(Constants.INTERNAL_STORAGE_PATH+"Android/"))
            return false;
        return true;
    }

    public static List<MediaModel> getAllMedia(String absPath){
        if(absPath == null)
            throw new NullPointerException("absPath is null");
        Log.e(TAG,absPath);
        List<MediaModel> mediaModels = new ArrayList<>();
        File album = new File(absPath);
        File[] files = album.listFiles(new MediaFileFilter());
        if(files == null) return mediaModels;
        for(File file : files){
            mediaModels.add(new MediaModel(file));
        }
        return mediaModels;
    }

    public static File getThumbnail(File file){
        for(File f : file.listFiles(new MediaFileFilter())){
            if(!f.isDirectory())
                return f;
        }
        return null;
    }

    public static void shareMediaModels(ArrayList<MediaModel> medias, Context context){
        ArrayList<Uri> files = new ArrayList<>();
        for(MediaModel m : medias){
            Uri uri = FileProvider.getUriForFile(context,
                    BuildConfig.APPLICATION_ID+".provider",m.getFile());
            files.add(uri);
        }
        Intent intent =  new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("*/*");
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        context.startActivity(intent);
    }

}
