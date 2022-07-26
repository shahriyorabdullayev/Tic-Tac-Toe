package com.shahriyor.tic_tac_toe.global

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.shahriyor.tic_tac_toe.R

open class BaseActivity: AppCompatActivity() {

    fun setupStatusBar() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

}