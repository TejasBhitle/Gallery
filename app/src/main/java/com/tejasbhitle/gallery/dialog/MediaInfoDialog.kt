package com.tejasbhitle.gallery.dialog

import android.content.Context

import com.tejasbhitle.gallery.R
import com.tejasbhitle.gallery.model.MediaModel

import androidx.appcompat.app.AlertDialog

object MediaInfoDialog {

    private fun getDetailsString(media: MediaModel): String {
        return "Name:\n" + media.file!!.name + "\n\n" +
                "Path:\n" + media.file!!.absolutePath + "\n\n" +
                "Size:\n" + media.fileSize + "\n\n"

    }

    fun showMediaInfoDialog(context: Context, media: MediaModel) {
        val builder = AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.details))
                .setMessage(getDetailsString(media))
                .setPositiveButton(R.string.okay) { dialog, which -> }
        builder.create().show()
    }
}
