package com.tejasbhitle.gallery.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.alexvasilkov.gestures.Settings
import com.alexvasilkov.gestures.views.GestureImageView
import com.bumptech.glide.Glide
import com.tejasbhitle.gallery.R
import com.tejasbhitle.gallery.model.MediaModel

class MediaPagerAdapter(
        var mediaModels: List<MediaModel>,
        var context: Context,
        var pagerOnClickListener: PagerOnClickListener?)
    : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val mediaModel = mediaModels[position]
        val view = LayoutInflater.from(context)
                .inflate(R.layout.pager_media_model, container, false)

        val imageView = view.findViewById<GestureImageView>(R.id.media_image)

        if (mediaModel.isVideoFile) {

            Glide.with(context)
                    .load(mediaModel.file)
                    .thumbnail(0.1f)
                    .into(imageView)

            imageView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(mediaModel.file!!.path), "video/*")
                context.startActivity(intent)
            }

        } else {

            Glide.with(context)
                    .load(mediaModel.file)
                    .into(imageView)

            imageView.controller.settings
                    .setMaxZoom(4f)
                    .setDoubleTapZoom(-1f) // Falls back to max zoom level
                    .setPanEnabled(true)
                    .setZoomEnabled(true)
                    .setDoubleTapEnabled(true)
                    .setRotationEnabled(true)
                    .setRestrictRotation(true)
                    .setOverscrollDistance(0f, 0f)
                    .setOverzoomFactor(2f)
                    .setFillViewport(false)
                    .setFitMethod(Settings.Fit.INSIDE).gravity = Gravity.CENTER

            imageView.setOnClickListener {
                if (pagerOnClickListener != null && !mediaModel.isVideoFile) {
                    pagerOnClickListener!!.onClick()
                }
            }
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int = mediaModels.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    interface PagerOnClickListener {
        fun onClick()
    }
}
