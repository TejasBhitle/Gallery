package com.tejasbhitle.gallery.model

import android.app.Activity
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.tejasbhitle.gallery.R

import java.io.File
import java.net.URLConnection

class MediaModel : AbstractItem<MediaModel, MediaModel.ViewHolder>, Parcelable {

    constructor(file: File?) {
        this.file = file
    }

    var file: File? = null
    private val TYPE_ID = 683881
    private val TAG = "MediaModel"

    val isVideoFile: Boolean
        get() {
            try {
                val mimeType = URLConnection.guessContentTypeFromName(this.file!!.path)
                return mimeType != null && mimeType.startsWith("video")
            } catch (e: StringIndexOutOfBoundsException) {
                Log.e(TAG, file!!.path)
                e.printStackTrace()
            }
            return false
        }

    val mimeType: String
        get() = if (isVideoFile) "video/*" else "image/*"

    val fileSize: String
        get() {
            var fileLen = file!!.length()
            if (fileLen < 1024)
                return fileLen.toString() + " b"
            fileLen /= 1024
            if (fileLen < 1024)
                return fileLen.toString() + " Kb"
            fileLen /= 1024
            if (fileLen < 1024)
                return fileLen.toString() + " Mb"
            fileLen /= 1024
            return fileLen.toString() + " Gb"
        }

    override fun getViewHolder(v: View): ViewHolder  = ViewHolder(v)

    override fun getType(): Int = TYPE_ID

    override fun getLayoutRes(): Int = R.layout.list_item_media


    inner class ViewHolder(view: View) : FastAdapter.ViewHolder<MediaModel>(view) {

        private val media_image: ImageView
        private val context: Context

        init {
            context = view.context
            media_image = view.findViewById(R.id.media_image)


            val displayMetrics = DisplayMetrics()
            (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)

            val width = displayMetrics.widthPixels
            val factor = 4
            media_image.layoutParams.height = width / factor

        }

        override fun bindView(item: MediaModel, payloads: List<Any>) {
            if (item.isVideoFile) {
                Glide.with(context)
                        .load(item.file)
                        .apply(RequestOptions().fitCenter().centerCrop())
                        .thumbnail(0.1f)
                        .into(media_image)
            } else {
                Glide.with(context)
                        .load(item.file)
                        .apply(RequestOptions().fitCenter().centerCrop())
                        .into(media_image)
            }
        }

        override fun unbindView(item: MediaModel) {}
    }

    override fun describeContents(): Int  = 0

    override fun writeToParcel(dest: Parcel, flags: Int) : Unit
            = dest.writeSerializable(this.file)

    protected constructor(`in`: Parcel) {
        this.file = `in`.readSerializable() as File
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MediaModel>
                = object : Parcelable.Creator<MediaModel> {
            override fun createFromParcel(source: Parcel): MediaModel {
                return MediaModel(source)
            }

            override fun newArray(size: Int): Array<MediaModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}
