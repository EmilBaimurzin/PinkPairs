package com.pink.game.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pink.game.R
import com.pink.game.core.library.ViewBindingDialog
import com.pink.game.databinding.DialogEndBinding
import com.pink.game.domain.other.AppPrefs
import com.pink.game.ui.choice.FragmentChoiceDirections

class DialogEnd: ViewBindingDialog<DialogEndBinding>(DialogEndBinding::inflate) {
    private val args: DialogEndArgs by navArgs()
    private val sp by lazy {
        AppPrefs(requireContext())
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Dialog_No_Border)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog!!.setCancelable(false)
        dialog!!.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                findNavController().popBackStack(R.id.fragmentMain, false, false)
                true
            } else {
                false
            }
        }

        val hours = args.time / 3600;
        val minutes = (args.time % 3600) / 60;
        val seconds = args.time % 60;

        binding.timeTextView.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)

        val bhours = sp.getBestTime(args.difficulty) / 3600;
        val bminutes = (sp.getBestTime(args.difficulty)  % 3600) / 60;
        val bseconds = sp.getBestTime(args.difficulty)  % 60;

        binding.bestTimeTextView.text = String.format("%02d:%02d:%02d", bhours, bminutes, bseconds)

        binding.buttonRestart.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentChoice)
            findNavController().navigate(FragmentChoiceDirections.actionFragmentChoiceToFragmentGame(args.difficulty))
        }
        binding.buttonMenu.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
        }
    }
}