package com.tejasbhitle.gallery.util

import java.io.File
import java.io.FileFilter

class ImageFileFilter : FileFilter {
    private val okFileExtensions = arrayOf("jpg", "jpeg", "png", "gif")

    override fun accept(file: File): Boolean {
        for (extension in okFileExtensions) {
            if (file.name.toLowerCase().endsWith(extension)) {
                return true
            }
        }
        return false
    }
}
