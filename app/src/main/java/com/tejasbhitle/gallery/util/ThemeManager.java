package com.tejasbhitle.gallery.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.tejasbhitle.gallery.R;

public class ThemeManager {

    private static final String TAG = "ThemeManager";

    public static int getTheme(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String themeString = prefs.getString(
                context.getString(R.string.prefs_theme),
                context.getString(R.string.light)
        );

        Log.e(TAG,themeString);

        int theme;
        switch (themeString){
            case "light_theme":
                theme = R.style.AppTheme_Light;
                break;
            case "dark_theme":
                theme = R.style.AppTheme_Dark;
                break;
            case "black_theme":
                theme = R.style.AppTheme_Black;
                break;
            default:
                theme = R.style.AppTheme_Light;
        }

        return theme;
    }

}
