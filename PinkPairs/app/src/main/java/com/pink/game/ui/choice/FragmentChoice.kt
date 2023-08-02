package com.pink.game.ui.choice

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.pink.game.databinding.FragmentChoiceBinding
import com.pink.game.domain.game.Difficulty
import com.pink.game.ui.other.ViewBindingFragment

class FragmentChoice: ViewBindingFragment<FragmentChoiceBinding>(FragmentChoiceBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonClose.setOnClickListener {
                findNavController().popBackStack()
            }
            buttonEasy.setOnClickListener {
                findNavController().navigate(FragmentChoiceDirections.actionFragmentChoiceToFragmentGame(Difficulty.EASY))
            }
            buttonNormal.setOnClickListener {
                findNavController().navigate(FragmentChoiceDirections.actionFragmentChoiceToFragmentGame(Difficulty.NORMAL))
            }
            buttonHard.setOnClickListener {
                findNavController().navigate(FragmentChoiceDirections.actionFragmentChoiceToFragmentGame(Difficulty.HARD))
            }
            buttonHarder.setOnClickListener {
                findNavController().navigate(FragmentChoiceDirections.actionFragmentChoiceToFragmentGame(Difficulty.HARDER))
            }
        }
    }
}