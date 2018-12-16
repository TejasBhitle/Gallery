package com.tejasbhitle.gallery.util

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.BaseColumns
import android.provider.MediaStore

import com.tejasbhitle.gallery.model.MediaModel

object UriHandler {

    fun getContentUri(context: Context, mediaModel: MediaModel): Uri? {
        return getContentUri(context, mediaModel.file!!.absolutePath, mediaModel.mimeType)
    }

    fun getContentUri(context: Context, path: String, mimeType: String): Uri? {

        val resolver = context.contentResolver
        val contentUri = MediaStore.Files.getContentUri("external")
        val cursor = resolver.query(
                contentUri,
                arrayOf(BaseColumns._ID),
                MediaStore.MediaColumns.DATA + " = ?",
                arrayOf(path), null
        ) ?: return Uri.parse(path)

        cursor.moveToFirst()
        if (cursor.isAfterLast) {
            cursor.close()
            // insert system media db
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DATA, path)
            values.put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            return context.contentResolver.insert(contentUri, values)
        } else {
            val id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
            val uri = ContentUris.withAppendedId(contentUri, id)
            cursor.close()
            return uri
        }
    }

}
