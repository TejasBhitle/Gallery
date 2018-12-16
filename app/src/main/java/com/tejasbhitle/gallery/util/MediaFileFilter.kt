package com.tejasbhitle.gallery.util

import java.io.File
import java.io.FileFilter

class MediaFileFilter : FileFilter {

    private val okFileExtensions = arrayOf("jpg", "jpeg", "png", "gif", "mp4", "avi", "mov")

    override fun accept(file: File): Boolean {
        for (extension in okFileExtensions) {
            if (file.name.toLowerCase().endsWith(extension)) {
                return true
            }
        }
        return false
    }
}
