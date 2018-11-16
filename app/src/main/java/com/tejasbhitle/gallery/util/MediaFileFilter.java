package com.tejasbhitle.gallery.util;

import java.io.File;
import java.io.FileFilter;

public class MediaFileFilter implements FileFilter {

    private final String[] okFileExtensions = new String[]
            { "jpg", "jpeg", "png", "gif","mp4", "avi", "mov" };

    @Override
    public boolean accept(File file) {
        for (String extension : okFileExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}
