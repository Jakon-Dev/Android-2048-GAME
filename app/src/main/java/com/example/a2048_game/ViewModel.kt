// GameViewModel.kt
package com.example.game2048

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

class GameViewModel : ViewModel() {
    private val _board = MutableStateFlow(Array(4) { IntArray(4) { 0 } })
    val board = _board.asStateFlow()

    init {
        resetGame()
    }

    fun resetGame() {
        val newBoard = Array(4) { IntArray(4) { 0 } }
        repeat(2) { addRandomTile(newBoard) }
        _board.value = newBoard
    }

    private fun addRandomTile(board: Array<IntArray>) {
        val empty = mutableListOf<Pair<Int, Int>>()
        for (i in 0..3) {
            for (j in 0..3) {
                if (board[i][j] == 0) empty.add(i to j)
            }
        }
        if (empty.isNotEmpty()) {
            val (i, j) = empty.random()
            board[i][j] = if (Random.nextFloat() < 0.9f) 2 else 4
        }
    }

    fun moveLeft() = move { row -> merge(row) }
    fun moveRight() = move { row -> merge(row.reversedArray()).reversedArray() }
    fun moveUp() = moveColumns { col -> merge(col) }
    fun moveDown() = moveColumns { col -> merge(col.reversedArray()).reversedArray() }

    private fun move(transform: (IntArray) -> IntArray) {
        val oldBoard = _board.value.map { it.copyOf() }.toTypedArray()
        val newBoard = Array(4) { row -> transform(oldBoard[row]) }
        if (!boardEquals(newBoard, oldBoard)) {
            addRandomTile(newBoard)
        }
        _board.value = newBoard
    }


    private fun moveColumns(transform: (IntArray) -> IntArray) {
        val oldBoard = _board.value.map { it.copyOf() }.toTypedArray()
        val newBoard = Array(4) { row -> transform(oldBoard[row]) }

        for (j in 0..3) {
            val col = IntArray(4) { i -> _board.value[i][j] }
            val newCol = transform(col)
            for (i in 0..3) {
                newBoard[i][j] = newCol[i]
            }
        }
        if (!boardEquals(newBoard, _board.value)) {
            addRandomTile(newBoard)
        }
        _board.value = newBoard
    }

    private fun merge(row: IntArray): IntArray {
        val newRow = row.filter { it != 0 }.toMutableList()
        var i = 0
        while (i < newRow.size - 1) {
            if (newRow[i] == newRow[i + 1]) {
                newRow[i] *= 2
                newRow.removeAt(i + 1)
            }
            i++
        }
        return newRow.toIntArray().copyOf(4)
    }

    private fun boardEquals(a: Array<IntArray>, b: Array<IntArray>): Boolean {
        for (i in 0..3) for (j in 0..3) if (a[i][j] != b[i][j]) return false
        return true
    }
}
