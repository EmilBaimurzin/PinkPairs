package com.pink.game.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.Slider
import com.pink.game.R
import com.pink.game.core.library.ViewBindingDialog
import com.pink.game.databinding.DialogSettingsBinding
import com.pink.game.domain.other.AppPrefs
import com.pink.game.ui.game.CallbackViewModel

class DialogSettings: ViewBindingDialog<DialogSettingsBinding>(DialogSettingsBinding::inflate) {
    private val viewModel: CallbackViewModel by activityViewModels()
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
                findNavController().popBackStack()
                viewModel.callback?.invoke()
                true
            } else {
                false
            }
        }

        binding.speedSlider.value = sp.getSoundValue().toFloat()
        binding.buttonClose.setOnClickListener {
            findNavController().popBackStack()
            viewModel.callback?.invoke()
        }

        binding.speedSlider.setCustomThumbDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.sound_thumb,
                null
            )!!
        )

        binding.speedSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {
                sp.setSoundValue(slider.value.toInt())
            }
        })

        binding.buttonApply.setOnClickListener {
            findNavController().popBackStack()
            viewModel.callback?.invoke()
        }
    }
}