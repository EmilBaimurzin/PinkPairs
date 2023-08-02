package com.pink.game.ui.game

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import android.media.ToneGenerator.MAX_VOLUME
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.pink.game.R
import com.pink.game.databinding.FragmentGameBinding
import com.pink.game.domain.game.GameAdapter
import com.pink.game.domain.other.AppPrefs
import com.pink.game.ui.choice.FragmentChoiceDirections
import com.pink.game.ui.other.ViewBindingFragment

class FragmentGame : ViewBindingFragment<FragmentGameBinding>(FragmentGameBinding::inflate) {
    private lateinit var gameAdapter: GameAdapter
    private lateinit var viewModel: GameViewModel
    private val callbackViewModel: CallbackViewModel by activityViewModels()
    private val args: FragmentGameArgs by navArgs()
    private val sp by lazy {
        AppPrefs(requireContext())
    }
    private var soundIdCorrect = 0
    private var soundIdWrong = 0
    private lateinit var soundPool: SoundPool

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        initAdapter()

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_GAME)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(audioAttributes)
            .build()
        soundIdCorrect = soundPool.load(requireContext(), R.raw.sound_correct, 1)
        soundIdWrong = soundPool.load(requireContext(), R.raw.sound_wrong, 1)
        setVolume()


        viewModel.winCallback = {
            end()
        }
        viewModel.soundCallback = {
            if (it) {
                soundPool.play(
                    soundIdCorrect,
                    (sp.getSoundValue().toFloat() * 2f / 10f),
                    (sp.getSoundValue().toFloat() * 2f / 10f),
                    1,
                    0,
                    1.0f
                )
            } else {
                soundPool.play(
                    soundIdWrong,
                    (sp.getSoundValue().toFloat() * 2f / 10f),
                    (sp.getSoundValue().toFloat() * 2f / 10f),
                    1,
                    0,
                    1.0f
                )
            }
        }

        callbackViewModel.callback = {
            setVolume()
            viewModel.pauseState = false
            viewModel.startTimer()
        }

        binding.settingsButton.setOnClickListener {
            viewModel.pauseState = true
            viewModel.stopTimer()
            findNavController().navigate(R.id.action_fragmentGame_to_dialogSettings)
        }

        binding.restartButton.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
            findNavController().navigate(R.id.action_fragmentMain_to_fragmentChoice)
            findNavController().navigate(
                FragmentChoiceDirections.actionFragmentChoiceToFragmentGame(
                    args.difficulty
                )
            )
        }

        binding.menuButton.setOnClickListener {
            findNavController().popBackStack(R.id.fragmentMain, false, false)
        }

        viewModel.list.observe(viewLifecycleOwner) {
            gameAdapter.items = it.toMutableList()
            gameAdapter.notifyDataSetChanged()
        }
        viewModel.timer.observe(viewLifecycleOwner) { totalSecs ->
            val hours = totalSecs / 3600;
            val minutes = (totalSecs % 3600) / 60;
            val seconds = totalSecs % 60;

            binding.timeTextView.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }

        if (viewModel.gameState && !viewModel.pauseState) {
            viewModel.startTimer()
        }

    }

    private fun setVolume() {
        soundPool.setVolume(soundIdCorrect, (sp.getSoundValue().toFloat() * 2f / 10f), (sp.getSoundValue() * 2 / 10).toFloat())
        soundPool.setVolume(soundIdWrong, (sp.getSoundValue().toFloat() * 2f / 10f), (sp.getSoundValue() * 2 / 10).toFloat())
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(
            this,
            GameViewModelFactory(args.difficulty)
        )[GameViewModel::class.java]
    }

    private fun end() {
        viewModel.stopTimer()
        viewModel.gameState = false
        if (sp.getBestTime(args.difficulty) > viewModel.timer.value!! || sp.getBestTime(args.difficulty) == 0) {
            sp.setBestTime(args.difficulty, viewModel.timer.value!!)
        }
        findNavController().navigate(
            FragmentGameDirections.actionFragmentGameToDialogEnd(
                args.difficulty,
                viewModel.timer.value!!
            )
        )
    }

    private fun initAdapter() {
        gameAdapter = GameAdapter {
            if (viewModel.list.value!!.find { it.closeAnimation } == null && viewModel.list.value!!.find { it.openAnimation } == null) {
                viewModel.openItem(it)
            }
        }
        with(binding.gameRV) {
            adapter = gameAdapter
            layoutManager = GridLayoutManager(requireContext(), 5).also {
                it.orientation = GridLayoutManager.VERTICAL
            }
            setHasFixedSize(true)
            itemAnimator = null
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopTimer()
        soundPool.release()
    }
}