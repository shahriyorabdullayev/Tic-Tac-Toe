package com.shahriyor.tic_tac_toe.fragment

import android.os.Bundle
import android.util.Log
import android.view.Gravity.CENTER
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.geniusforapp.fancydialog.builders.FancyDialogBuilder
import com.shahriyor.tic_tac_toe.R
import com.shahriyor.tic_tac_toe.data.GameDetails
import com.shahriyor.tic_tac_toe.databinding.FragmentPlayBinding
import com.shahriyor.tic_tac_toe.global.BaseFragment
import com.shahriyor.tic_tac_toe.utils.viewBinding
import com.shahriyor.tic_tac_toe.viewModels.PlayViewModel


class PlayFragment : BaseFragment(R.layout.fragment_play) {

    private val binding by viewBinding { FragmentPlayBinding.bind(it) }

    lateinit var playViewModel: PlayViewModel
    lateinit var gameDetails: GameDetails
    lateinit var playerPiece: String
    private var playedMoves: MutableList<String> =
        mutableListOf("A", "A", "A", "A", "A", "A", "A", "A", "A")
    var playerMove: Boolean = false

    lateinit var btnList: Array<ImageView>

    private val args: PlayFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(this)[PlayViewModel::class.java]
    }


    override fun onStart() {
        super.onStart()

        btnList = arrayOf(
            binding.btn1,
            binding.btn2,
            binding.btn3,
            binding.btn4,
            binding.btn5,
            binding.btn6,
            binding.btn7,
            binding.btn8,
            binding.btn9)

        playViewModel.getRoomDetails(args.key)
        playViewModel.getRealtimeDetails(args.key, args.player)
        playViewModel.gameDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                gameDetails = it
                Log.d("css", it.toString())
                if (args.player == 1) {
                    playerPiece = gameDetails.firstPlayerChar!!
                    if (gameDetails.firstPlayerMove == true) {
                        playerMove = true
                    }
                } else if (args.player == 2) {
                    playerPiece = gameDetails.secondPlayerChar!!
                    if (gameDetails.secondPlayerMove == true) {
                        playerMove = true
                    }
                }
                binding.Player1.text = gameDetails.firstPlayerName
                binding.Player2.text = gameDetails.secondPlayerName
            }
        }

        playViewModel.playerMove.observe(viewLifecycleOwner) {
            if (it) {
                btnOnClick()
                binding.turn.text = "Your" + " turn"

            } else {
                setImageViewUnClickable()
                binding.turn.text = "Opponent's" + " turn"

            }
        }

        playViewModel.moves.observe(viewLifecycleOwner) {
            playedMoves = it
            refreshPlayedMoves(it)
        }

        playViewModel.scorePlayerFirst.observe(viewLifecycleOwner) {
            binding.p1Winning.text = it.toString()
        }
        playViewModel.scorePlayerSecond.observe(viewLifecycleOwner) {
            binding.p2Winning.text = it.toString()
        }

        playViewModel.checkStatusGame.observe(viewLifecycleOwner) {
            when (it) {
                "Draw" -> {
                    resultDialog(isWin = false, isLose = false, isDraw = true, isQuit = false)
                }
                "Win" -> {
                    resultDialog(isWin = true, isLose = false, isDraw = false, isQuit = false)
                }
                "Lose" -> {
                    resultDialog(isWin = false, isLose = true, isDraw = false, isQuit = false)
                }
            }
        }

        binding.btnQuit.setOnClickListener {
            resultDialog(isWin = false, isLose = false, isDraw = false, isQuit = true)
        }
        btnOnClick()
    }

    private fun btnOnClick() {
        for (i in 0..8) {
            if (playedMoves[i] == "A") {
                btnList[i].setOnClickListener {
                    innerbtnOnClick(btnList[i], i + 1)
                }
            }
        }
    }

    fun innerbtnOnClick(btn: ImageView, btnPos: Int) {
        if (playerPiece == "X") {
            btn.setImageResource(R.drawable.ic_cross_yellow)
        }
        if (playerPiece == "O") {
            btn.setImageResource(R.drawable.ic_circle_secondary)
        }
        playViewModel.btnClick(btnPos, args.player)
    }

    private fun refreshPlayedMoves(moves: MutableList<String>) {
        for (i in moves.indices) {

            for (j in 0..8) {
                if (i == j && moves[i] == "X") {
                    btnList[j].setImageResource(R.drawable.ic_cross_yellow)
                } else if (i == j && moves[i] == "O") {
                    btnList[j].setImageResource(R.drawable.ic_circle_secondary)
                } else if (i == j && moves[i] == "A") {
                    btnList[j].setImageDrawable(null)
                }
            }
        }
    }

    private fun setImageViewUnClickable() {
        for (i in 0..8) {
            btnList[i].setOnClickListener(null)
        }
    }


    private fun resultDialog(
        isWin: Boolean,
        isLose: Boolean,
        isDraw: Boolean,
        isQuit: Boolean,
    ) {
        if (isWin) {
            val dialog = FancyDialogBuilder(requireContext(), R.style.CustomDialog)
                .withImageIcon(R.drawable.trophy_win)
                .withTitleTypeFace(R.font.otomanopeeone_regular)
                .withSubTitleTypeFace(R.font.otomanopeeone_regular)
                .withActionPositiveTypeFace(R.font.otomanopeeone_regular)
                .withActionNegativeTypeFace(R.font.otomanopeeone_regular)
                .withTextGravity(CENTER)
                .withPanelGravity(CENTER)
                .withTitle("Win")
                .withSubTitle("Congratulations!! You won the Game.")
                .withPositive("PLAY AGAIN") { _, dialog ->
                    dialog.dismiss()
                    playViewModel.playAgain()
                }
                .withNegative("LEAVE GAME") { _, dialog ->
                    dialog.dismiss()
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_playFragment_to_createJoinFragment)
                }
            dialog.show()
        } else if (isLose) {
            val dialog = FancyDialogBuilder(requireContext(), R.style.CustomDialog)
                .withImageIcon(R.drawable.defeat)
                .withTitleTypeFace(R.font.otomanopeeone_regular)
                .withSubTitleTypeFace(R.font.otomanopeeone_regular)
                .withActionPositiveTypeFace(R.font.otomanopeeone_regular)
                .withActionNegativeTypeFace(R.font.otomanopeeone_regular)
                .withTextGravity(CENTER)
                .withPanelGravity(CENTER)
                .withTitle("Defeat")
                .withSubTitle("Oh No!! You just lost the Game.")
                .withPositive("PLAY AGAIN") { _, dialog ->
                    dialog.dismiss()
                    playViewModel.playAgain()
                }
                .withNegative("LEAVE GAME") { view, dialog ->
                    dialog.dismiss()
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_playFragment_to_createJoinFragment)

                }
            dialog.show()
        } else if (isDraw) {
            val dialog = FancyDialogBuilder(requireContext(), R.style.CustomDialog)
                .withImageIcon(R.drawable.tic_tac_toe)
                .withTitleTypeFace(R.font.otomanopeeone_regular)
                .withSubTitleTypeFace(R.font.otomanopeeone_regular)
                .withActionPositiveTypeFace(R.font.otomanopeeone_regular)
                .withActionNegativeTypeFace(R.font.otomanopeeone_regular)
                .withTextGravity(CENTER)
                .withPanelGravity(CENTER)
                .withTitle("Draw")
                .withSubTitle("The Game is Draw")
                .withPositive("PLAY AGAIN") { _, dialog ->
                    dialog.dismiss()
                    playViewModel.playAgain()
                }
                .withNegative("LEAVE GAME") { _, dialog ->
                    dialog.dismiss()
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_playFragment_to_createJoinFragment)
                }
            dialog.show()
        } else if (isQuit){
            val dialog = FancyDialogBuilder(requireContext(), R.style.CustomDialog)
                .withImageIcon(R.drawable.tic_tac_toe)
                .withTitleTypeFace(R.font.otomanopeeone_regular)
                .withSubTitleTypeFace(R.font.otomanopeeone_regular)
                .withActionPositiveTypeFace(R.font.otomanopeeone_regular)
                .withActionNegativeTypeFace(R.font.otomanopeeone_regular)
                .withTextGravity(CENTER)
                .withPanelGravity(CENTER)
                .withTitle("Exit Game")
                .withSubTitle("Are you sure you want to leave this Game.")
                .withPositive("YES") { _, dialog ->
                    findNavController()
                        .navigate(R.id.action_playFragment_to_createJoinFragment)
                    dialog.dismiss()
                }
                .withNegative("NO") { view, dialog -> dialog.dismiss() }
            dialog.show()
        }


    }

}