package com.shahriyor.tic_tac_toe

import android.animation.Animator
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.shahriyor.tic_tac_toe.databinding.ActivitySplashBinding
import com.shahriyor.tic_tac_toe.global.BaseActivity
import com.shahriyor.tic_tac_toe.global.BaseFragment
import com.shahriyor.tic_tac_toe.utils.ConnectivityReceiver
import com.shahriyor.tic_tac_toe.utils.callMainActivity

class SplashScreen : BaseActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var dialog: Dialog? = null

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupStatusBar()

        animation()

    }

    private fun animation() {
        binding.lottie.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
            }
            override fun onAnimationEnd(p0: Animator?) {
                callMainActivity()
            }
            override fun onAnimationCancel(p0: Animator?) {
            }
            override fun onAnimationRepeat(p0: Animator?) {
            }
        })
    }

    // Timer for splash screen
    private fun countDownTimer() {
        object : CountDownTimer(
            1000,
            3000
        ) {
            override fun onTick(p0: Long) {}
            override fun onFinish() {
                callMainActivity()
            }
        }.start()
    }


    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    private fun showDialog(isConnected: Boolean) {
        if (!isConnected) {
            dialog = Dialog(this)
            dialog?.setContentView(R.layout.dialog_check_connection)
            dialog?.setCancelable(false)
            dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog?.show()
        } else {
            dialog?.dismiss()
            animation()
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showDialog(isConnected)
    }
}