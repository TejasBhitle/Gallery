package com.tejasbhitle.gallery.dialog;

import android.content.Context;
import android.content.DialogInterface;

import com.tejasbhitle.gallery.R;
import com.tejasbhitle.gallery.model.MediaModel;

import androidx.appcompat.app.AlertDialog;

public class MediaInfoDialog {

    private static String getDetailsString(MediaModel media){
        return "Name:\n"+media.getFile().getName()+"\n\n"+
                "Path:\n"+media.getFile().getAbsolutePath()+"\n\n"+
                "Size:\n"+media.getFileSize()+"\n\n";

    }

    public static void showMediaInfoDialog(Context context, MediaModel media){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.details))
                .setMessage(getDetailsString(media))
                .setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();
    }
}
