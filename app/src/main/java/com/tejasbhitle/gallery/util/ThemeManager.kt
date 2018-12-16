package com.tejasbhitle.gallery.util

import android.content.Context
import android.preference.PreferenceManager

import com.tejasbhitle.gallery.R

object ThemeManager {

    fun getTheme(context: Context): Int {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val themeString = prefs.getString(context.getString(R.string.prefs_theme), context.getString(R.string.light))
        return when (themeString) {
            "dark_theme" -> R.style.AppTheme_Dark
            "black_theme" -> R.style.AppTheme_Black
            else -> R.style.AppTheme_Light
        }
    }

}
