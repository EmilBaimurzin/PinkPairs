package com.pink.game.domain.other

import android.content.Context
import com.pink.game.domain.game.Difficulty

class AppPrefs(context: Context) {
    private val sp = context.getSharedPreferences("SP", Context.MODE_PRIVATE)
    fun getBestTime(difficulty: Difficulty): Int = sp.getInt("BEST${difficulty.name}", 0)
    fun setBestTime(difficulty: Difficulty, value: Int) =
        sp.edit().putInt("BEST${difficulty.name}", value).apply()

    fun getSoundValue(): Int = sp.getInt("SOUND", 3)
    fun setSoundValue(value: Int) = sp.edit().putInt("SOUND", value).apply()
}