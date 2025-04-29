package com.jakondev.a2048_game.viewmodel

class SampleBoards {
    val empty = Array(4) { IntArray(4) { 0 } }

    val full = Array(4) { IntArray(4) { 2 } }

    val almostLost = arrayOf(
        intArrayOf(2, 4, 2, 4),
        intArrayOf(4, 2, 4, 2),
        intArrayOf(16, 8, 64, 16),
        intArrayOf(0, 0, 32, 0)
    )

    val almostWon = arrayOf(
        intArrayOf(0, 0, 0, 0),
        intArrayOf(0, 0, 0, 0),
        intArrayOf(0, 0, 0, 1024),
        intArrayOf(0, 0, 0, 1024),
    )

    val maxTable = arrayOf(
        intArrayOf(2, 2, 4, 8),
        intArrayOf(128, 64, 32, 16),
        intArrayOf(256, 512, 1024, 2048),
        intArrayOf(32768, 16384, 8192, 4092),
    )
}
