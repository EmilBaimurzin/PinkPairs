package com.pink.game.domain.game

data class GameItem(
    val value: Int,
    val bgValue: Int,
    var isOpened: Boolean = false,
    var openAnimation: Boolean = false,
    var closeAnimation: Boolean = false
)