package com.pink.game.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pink.game.domain.game.Difficulty
import com.pink.game.domain.game.GameItem
import com.pink.game.domain.game.GameRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel(private val difficulty: Difficulty) : ViewModel() {
    private val repository = GameRepository()
    private var foundPairs = mutableListOf<Pair<Int, Int>>()
    private var gameScope = CoroutineScope(Dispatchers.Default)
    var gameState = true
    var winCallback: (() -> Unit)? = null
    var soundCallback: ((Boolean) -> Unit)? = null
    var pauseState = false
    private val _list = MutableLiveData(
        repository.generateList(
            difficulty
        )
    )
    val list: LiveData<List<GameItem>> = _list

    private val _timer = MutableLiveData(0)
    val timer: LiveData<Int> = _timer

    fun startTimer() {
        gameScope = CoroutineScope(Dispatchers.Default)
        gameScope.launch {
            while (true) {
                delay(1000)
                _timer.postValue(_timer.value!! + 1)
            }
        }
    }

    fun stopTimer() {
        gameScope.cancel()
    }

    fun openItem(index: Int) {
        viewModelScope.launch {
            val newList = _list.value!!
            newList[index].openAnimation = true
            _list.postValue(newList)
            delay(410)
            newList[index].openAnimation = false
            newList[index].isOpened = true
            val filteredList = newList.filter { it.isOpened && !foundPairs.contains(it.value to it.bgValue)}
            if (filteredList.size == 2) {
                val isSame = filteredList.distinct()
                if (isSame.size == 1) {
                    soundCallback?.invoke(true)
                    foundPairs.add(newList[index].value to newList[index].bgValue)
                    val difficultyValue = when (difficulty) {
                        Difficulty.EASY -> 10
                        Difficulty.NORMAL -> 20
                        Difficulty.HARD -> 30
                        Difficulty.HARDER -> 40
                    }
                    if (foundPairs.size == difficultyValue / 2) {
                        winCallback?.invoke()
                    }
                } else {
                    soundCallback?.invoke(false)
                    val newFilteredList = newList.filter { it.isOpened && !foundPairs.contains(it.value to it.bgValue)}
                    newFilteredList.forEach {
                        newList[newList.indexOf(it)].closeAnimation = true
                    }
                    _list.postValue(newList)
                    delay(410)
                    newFilteredList.map { it.closeAnimation = true }
                    newFilteredList.forEach {
                        newList[newList.indexOf(it)].closeAnimation = false
                        newList[newList.indexOf(it)].isOpened = false
                    }
                    _list.postValue(newList)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        gameScope.cancel()
    }
}

class GameViewModelFactory(private val difficulty: Difficulty) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GameViewModel(difficulty) as T
    }
}