package com.tejasbhitle.gallery.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tejasbhitle.gallery.util.ThemeManager

open class BaseActivity : AppCompatActivity() {

    internal var theme: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        theme = ThemeManager.getTheme(this)
        setTheme(theme)
    }

    override fun onResume() {
        if (isThemeChanged()) {
            recreate()
        }
        super.onResume()
    }

    private fun isThemeChanged(): Boolean {
        return theme != ThemeManager.getTheme(this)
    }

}