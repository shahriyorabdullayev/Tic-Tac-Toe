package com.shahriyor.tic_tac_toe.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.shahriyor.tic_tac_toe.R
import com.shahriyor.tic_tac_toe.data.GameDetails
import com.shahriyor.tic_tac_toe.databinding.FragmentCreateBinding
import com.shahriyor.tic_tac_toe.global.BaseFragment
import com.shahriyor.tic_tac_toe.utils.viewBinding
import com.shahriyor.tic_tac_toe.viewModels.CreateViewModel


class CreateFragment : BaseFragment(R.layout.fragment_create) {

    private val binding by viewBinding { FragmentCreateBinding.bind(it) }

    lateinit var createViewModel: CreateViewModel
    lateinit var key: String
    var name: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createViewModel = ViewModelProvider(this)[CreateViewModel::class.java]
    }


    override fun onStart() {
        super.onStart()

        binding.copy.setOnClickListener {
            val clipboardManager =
                requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", binding.txtroomid.text.toString().trim())
            clipboardManager.setPrimaryClip(clipData)
            Toast.makeText(context, "RoomID Copied Successfully", Toast.LENGTH_LONG).show()

        }

        createViewModel.result.observe(viewLifecycleOwner) {
            if (it == null) {
                Toast.makeText(context, "Room Created Successfully", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, it.message.toString(), Toast.LENGTH_LONG).show()
            }
        }

        createViewModel.join.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.secondPlayerName != null) {
                    Toast.makeText(context, it.secondPlayerName + " Joined", Toast.LENGTH_LONG).show()
                    binding.progressBar.visibility = View.GONE
                    binding.waiting.visibility = View.GONE
                    //binding.startButton.visibility = View.VISIBLE
                    val action = CreateFragmentDirections.actionCreateFragmentToPlayFragment(key, 1)
                    findNavController().navigate(action)
                }
            }
        })

        createViewModel.keyDB.observe(viewLifecycleOwner) {
            if (it != null) {
                key = it
            }
        }



        binding.roomButton.setOnClickListener {
            name = binding.textFieldName.editText?.text.toString().trim()
            if (name.isNullOrEmpty()) {
                Toast.makeText(context,
                    "We don't know your name.Please Enter It.",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val gameDetails = GameDetails()
            createViewModel.createRoom(name!!, gameDetails)
            binding.txtroomid.text = gameDetails.roomID

            createViewModel.realtimeUpdate()
            hideKeyboard()
        }

//        binding.startButton.setOnClickListener {
//            val action = CreateFragmentDirections.actionCreateFragmentToPlayFragment(key, 1)
//            Navigation.findNavController(it).navigate(action)
//        }

    }

    private fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}