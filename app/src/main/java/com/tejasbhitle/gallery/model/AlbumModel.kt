package com.tejasbhitle.gallery.model

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.tejasbhitle.gallery.R
import com.tejasbhitle.gallery.util.FileHandler

import java.io.File

class AlbumModel(val file: File, val numOfFiles: Int)
    : AbstractItem<AlbumModel, AlbumModel.ViewHolder>() {

    companion object {
        private val TAG = "AlbumModel"
        private val TYPE_ID = 4838481
    }

    val name: String
        get() = this.file.name

    val filePath: String
        get() = this.file.absolutePath


    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override fun getType(): Int {
        return TYPE_ID
    }

    override fun getLayoutRes(): Int {
        return R.layout.list_item_album
    }

    class ViewHolder internal constructor(view: View) : FastAdapter.ViewHolder<AlbumModel>(view) {

        private val album_name: TextView
        private val album_size: TextView//,album_path;
        private val album_image: ImageView
        private val context: Context

        init {
            context = view.context
            album_name = view.findViewById(R.id.album_name)
            album_size = view.findViewById(R.id.album_size)
            album_image = view.findViewById(R.id.album_image)

            val displayMetrics = DisplayMetrics()
            (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
            album_image.layoutParams.height = displayMetrics.heightPixels / 4
            album_image.requestLayout()
        }

        override fun bindView(item: AlbumModel, payloads: List<Any>) {

            album_name.text = item.name
            album_size.text = "(" + item.numOfFiles.toString() + ")"

            val media = MediaModel(FileHandler.getThumbnail(item.file))
            if (media.isVideoFile) {
                Glide.with(context)
                        .load(media.file)
                        .apply(RequestOptions().fitCenter().centerCrop())
                        .thumbnail(0.1f)
                        .into(album_image)
            } else {
                Glide.with(context)
                        .load(media.file)
                        .apply(RequestOptions().fitCenter().centerCrop())
                        .into(album_image)
            }
        }

        override fun unbindView(item: AlbumModel) {}
    }

}
