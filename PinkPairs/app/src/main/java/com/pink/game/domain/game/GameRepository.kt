package com.pink.game.domain.game

class GameRepository {
    fun generateList(difficulty: Difficulty): List<GameItem> {
        val listToReturn = mutableListOf<GameItem>()
        when (difficulty) {
            Difficulty.EASY -> {
                repeat(10) {
                    val value = when (it + 1) {
                        in (1..2) -> 1
                        in (3..4) -> 2
                        in (5..6) -> 3
                        in (7..8) -> 4
                        else -> 5
                    }
                    listToReturn.add(GameItem(value, 1))
                }
            }

            Difficulty.NORMAL -> {
                repeat(2) { bgValue ->
                    repeat(10) {
                        val value = when (it + 1) {
                            in (1..2) -> 1
                            in (3..4) -> 2
                            in (5..6) -> 3
                            in (7..8) -> 4
                            else -> 5
                        }
                        listToReturn.add(GameItem(value, bgValue + 1))
                    }
                }
            }

            Difficulty.HARD -> {
                repeat(3) { bgValue ->
                    repeat(10) {
                        val value = when (it + 1) {
                            in (1..2) -> 1
                            in (3..4) -> 2
                            in (5..6) -> 3
                            in (7..8) -> 4
                            else -> 5
                        }
                        listToReturn.add(GameItem(value, bgValue + 1))
                    }
                }
            }

            Difficulty.HARDER -> {
                repeat(4) { bgValue ->
                    repeat(10) {
                        val value = when (it + 1) {
                            in (1..2) -> 1
                            in (3..4) -> 2
                            in (5..6) -> 3
                            in (7..8) -> 4
                            else -> 5
                        }
                        listToReturn.add(GameItem(value, bgValue + 1))
                    }
                }
            }
        }
        listToReturn.shuffle()
        return listToReturn
    }
}