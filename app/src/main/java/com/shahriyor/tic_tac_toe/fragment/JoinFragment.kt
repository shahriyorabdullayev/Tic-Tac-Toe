package com.shahriyor.tic_tac_toe.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.shahriyor.tic_tac_toe.R
import com.shahriyor.tic_tac_toe.data.GameDetails
import com.shahriyor.tic_tac_toe.databinding.FragmentJoinBinding
import com.shahriyor.tic_tac_toe.global.BaseFragment
import com.shahriyor.tic_tac_toe.utils.viewBinding
import com.shahriyor.tic_tac_toe.viewModels.JoinViewModel


class JoinFragment : BaseFragment(R.layout.fragment_join) {

    private val binding by viewBinding { FragmentJoinBinding.bind(it) }

    lateinit var joinViewModel: JoinViewModel
    var name: String? = null
    var etrRoomID: String? = null
    private var key: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        joinViewModel = ViewModelProvider(this)[JoinViewModel::class.java]
    }


    override fun onStart() {
        super.onStart()
        var gameDetails = GameDetails()

        binding.startGame.setOnClickListener {
            name = binding.player2name.editText?.text.toString().trim()
            etrRoomID = binding.roomId.editText?.text.toString().trim()

            if (name.isNullOrEmpty() || etrRoomID.isNullOrEmpty()) {
                Toast.makeText(context, "Name or RoomID missing...", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            joinViewModel.getRoomDetails(gameDetails, name!!, etrRoomID!!)
        }

        binding.paste.setOnClickListener {
            val myClipboard: ClipboardManager? =
                context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val abc: ClipData? = myClipboard?.primaryClip
            val item = abc?.getItemAt(0)
            binding.roomId.editText?.setText(item?.text.toString())
        }

        joinViewModel.gameDetails.observe(viewLifecycleOwner) {
            gameDetails = it
//            Toast.makeText(context, it.toString(), Toast.LENGTH_LONG).show()
            if (name == it.secondPlayerName) {
                val action = JoinFragmentDirections.actionJoinFragmentToPlayFragment(key!!, 2)
                Navigation.findNavController(requireView()).navigate(action)
            }
            if (name != it.secondPlayerName) {
                Toast.makeText(context, "RoomID Invalid", Toast.LENGTH_LONG).show()
            }
        }

        joinViewModel.keyDB.observe(viewLifecycleOwner) {
            if (it != null) {
                key = it
            }
        }
    }
}