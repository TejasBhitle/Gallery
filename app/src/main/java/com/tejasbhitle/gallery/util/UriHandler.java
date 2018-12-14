package com.tejasbhitle.gallery.util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;

import com.tejasbhitle.gallery.model.MediaModel;

public class UriHandler {

    public static Uri getContentUri(Context context, MediaModel mediaModel){
        return getContentUri(context, mediaModel.getFile().getAbsolutePath(), mediaModel.getMimeType());
    }

    public static Uri getContentUri(Context context, String path, String mimeType){

        ContentResolver resolver = context.getContentResolver();
        Uri contentUri = MediaStore.Files.getContentUri("external");
        Cursor cursor = resolver.query(
                contentUri,
                new String[]{BaseColumns._ID},
                MediaStore.MediaColumns.DATA + " = ?",
                new String[]{path}, null
        );

        if(cursor == null) return Uri.parse(path);

        cursor.moveToFirst();
        if (cursor.isAfterLast()) {
            cursor.close();
            // insert system media db
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, path);
            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType);
            return context.getContentResolver().insert(contentUri, values);
        } else {
            long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
            Uri uri = ContentUris.withAppendedId(contentUri, id);
            cursor.close();
            return uri;
        }
    }

}
