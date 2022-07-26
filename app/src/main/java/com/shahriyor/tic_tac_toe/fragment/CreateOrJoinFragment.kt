package com.shahriyor.tic_tac_toe.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.shahriyor.tic_tac_toe.R
import com.shahriyor.tic_tac_toe.databinding.FragmentCreateOrJoinBinding
import com.shahriyor.tic_tac_toe.global.BaseFragment
import com.shahriyor.tic_tac_toe.utils.viewBinding


class CreateOrJoinFragment : BaseFragment(R.layout.fragment_create_or_join) {

    private val binding by viewBinding { FragmentCreateOrJoinBinding.bind(it) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnCreate.setOnClickListener {
                findNavController().navigate(R.id.action_createJoinFragment_to_createFragment)
            }

            btnJoin.setOnClickListener {
                findNavController().navigate(R.id.action_createJoinFragment_to_joinFragment)
            }
        }
    }

}