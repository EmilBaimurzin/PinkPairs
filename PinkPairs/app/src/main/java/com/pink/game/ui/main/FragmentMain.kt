package com.pink.game.ui.main

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.pink.game.R
import com.pink.game.databinding.FragmentMainBinding
import com.pink.game.ui.game.CallbackViewModel
import com.pink.game.ui.other.ViewBindingFragment

class FragmentMain : ViewBindingFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {
    private val callbackViewModel: CallbackViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonPlay.setOnClickListener {
                findNavController().navigate(R.id.action_fragmentMain_to_fragmentChoice)
            }
            buttonSettings.setOnClickListener {
                callbackViewModel.callback = null
                findNavController().navigate(R.id.action_fragmentMain_to_dialogSettings)
            }
            buttonExit.setOnClickListener {
                requireActivity().finish()
            }
        }
        binding.privacyText.setOnClickListener {
            requireActivity().startActivity(
                Intent(
                    ACTION_VIEW,
                    Uri.parse("https://www.google.com")
                )
            )
        }
    }
}