package com.tejasbhitle.gallery.util

import android.annotation.TargetApi
import android.content.Context
import android.util.Log

import com.tejasbhitle.gallery.enums.Sort
import com.tejasbhitle.gallery.model.AlbumModel
import com.tejasbhitle.gallery.R
import com.tejasbhitle.gallery.model.MediaModel

import java.io.File
import java.util.ArrayList
import java.util.Comparator

import androidx.preference.PreferenceManager

object FileHandler {

    private val TAG = "FileHandler"
    private var showHidden = false

    fun getImageAlbumsUtil(context: Context, path: String, albumSortBy: Sort): List<AlbumModel> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        showHidden = prefs.getBoolean(
                context.getString(R.string.prefs_show_hidden),
                false
        )
        var albums = getImageAlbums(path)
        albums = albums.sortedWith(getComparator(albumSortBy))
        return albums
    }

    @TargetApi(24)
    private fun getComparator(sort: Sort): Comparator<AlbumModel>{
        return when (sort) {
            Sort.NAME_ASCEND ->
                kotlin.Comparator {
                    o1: AlbumModel, o2:AlbumModel ->
                    val result = o1.name.compareTo(o2.name)
                    if (result == 0) 0 else result / Math.abs(result)
                }
            Sort.NAME_DESCEND ->
                kotlin.Comparator {
                    o1: AlbumModel, o2:AlbumModel ->
                    val result = o1.name.compareTo(o2.name)
                    if (result == 0) 0 else -(result / Math.abs(result))
                }
            Sort.DATE_ASCEND ->
                kotlin.Comparator {
                    o1: AlbumModel, o2:AlbumModel ->
                    Math.toIntExact(o1.file.lastModified() - o2.file.lastModified())
                }
            Sort.DATE_DESCEND ->
                kotlin.Comparator {
                    o1: AlbumModel, o2:AlbumModel ->
                    -Math.toIntExact(o1.file.lastModified() - o2.file.lastModified())
                }
        }
    }

    private fun getImageAlbums(path: String): List<AlbumModel> {
        val albums = ArrayList<AlbumModel>()
        val BASEDIR = File(path)

        val files = BASEDIR.listFiles()
        for (file in files!!) {
            if (isAlbumValid(file)) {
                val imagesFiles = file.listFiles(MediaFileFilter())
                if (imagesFiles!!.isNotEmpty()) {
                    albums.add(AlbumModel(file, imagesFiles.size))
                }
                albums.addAll(getImageAlbums(path + '/'.toString() + file.name))
            }
        }
        return albums
    }

    private fun isAlbumValid(file: File): Boolean {
        if (!file.isDirectory) return false
        if (!showHidden)
            if (file.name.startsWith("."))
                return false
        return !(file.absolutePath.startsWith(Constants.INTERNAL_STORAGE_PATH + "Android/"))
    }

    fun getAllMedia(absPath: String): List<MediaModel> {
        Log.d(TAG, absPath)
        val mediaModels = ArrayList<MediaModel>()
        val album = File(absPath)
        val files = album.listFiles(MediaFileFilter()) ?: return mediaModels
        for (file in files)
            mediaModels.add(MediaModel(file))
        return mediaModels
    }

    fun getThumbnail(file: File): File? {
        for (f in file.listFiles(MediaFileFilter())!!)
            if (!f.isDirectory)
                return f
        return null
    }

}
