package com.shahriyor.tic_tac_toe

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import com.shahriyor.tic_tac_toe.global.BaseActivity
import com.shahriyor.tic_tac_toe.utils.ConnectivityReceiver

class MainActivity : BaseActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupStatusBar()
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
        }
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showDialog(isConnected)
    }


}