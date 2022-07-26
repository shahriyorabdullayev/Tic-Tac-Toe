package com.shahriyor.tic_tac_toe.utils

import android.app.Activity
import android.content.Intent
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.shahriyor.tic_tac_toe.MainActivity
import com.shahriyor.tic_tac_toe.R
import com.shahriyor.tic_tac_toe.SplashScreen


fun SplashScreen.callMainActivity() {
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    finish()
}

fun Toast.showCustomToast(image: Int, message: String, activity: Activity, gravity: Int) {
    val layout = activity.layoutInflater.inflate(R.layout.custom_toast, activity.findViewById(R.id.toast_layput_container))

    val toastMessage = layout.findViewById<TextView>(R.id.toast_message)
    toastMessage.text = message

    val toastImage = layout.findViewById<ImageView>(R.id.toast_image)
    toastImage.setImageResource(image)

    this.apply {
        setGravity(gravity, 0, 60)
        duration = Toast.LENGTH_SHORT
        view = layout
        show()
    }
}