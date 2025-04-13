package com.example.game2048

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.random.Random

// ViewModel que gestiona el estado del juego 2048.
class GameViewModel : ViewModel() {

    // Estado del tablero (4x4), representado como una matriz de enteros.
    private val _board = MutableStateFlow(Array(4) { IntArray(4) { 0 } })
    val board = _board.asStateFlow()

    // Puntuación actual del jugador.
    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    // Indica si el juego ha terminado.
    private val _isGameOver = MutableStateFlow(false)
    val isGameOver = _isGameOver.asStateFlow()

    // Al inicializar el ViewModel, se arranca el juego.
    init {
        resetGame()
    }

    // Reinicia el tablero y la puntuación.
    fun resetGame() {
        val newBoard = Array(4) { IntArray(4) { 0 } }
        _score.value = 0
        _isGameOver.value = false
        repeat(2) { addRandomTile(newBoard) } // Añade dos fichas al inicio
        _board.value = newBoard
    }

    // Añade una ficha aleatoria (2 o 4) en una casilla vacía.
    private fun addRandomTile(board: Array<IntArray>) {
        val empty = mutableListOf<Pair<Int, Int>>()

        // Busca todas las posiciones vacías (valor 0)
        for (i in 0..3) {
            for (j in 0..3) {
                if (board[i][j] == 0) empty.add(i to j)
            }
        }

        // Si hay huecos, coloca una ficha nueva
        if (empty.isNotEmpty()) {
            val (i, j) = empty.random()
            board[i][j] = if (Random.nextFloat() < 0.9f) 2 else 4
        }
    }

    // Funciones públicas para mover en las 4 direcciones
    fun moveLeft() = move { row -> merge(row) }
    fun moveRight() = move { row -> merge(row.reversedArray()).reversedArray() }
    fun moveUp() = moveColumns { col -> merge(col) }
    fun moveDown() = moveColumns { col -> merge(col.reversedArray()).reversedArray() }

    // Movimiento horizontal: transforma cada fila con una función (por ej. merge)
    private fun move(transform: (IntArray) -> IntArray) {
        val newBoard = Array(4) { row -> transform(_board.value[row]) }

        // Si hubo cambios en el tablero, añade una ficha nueva
        if (!boardEquals(newBoard, _board.value)) {
            addRandomTile(newBoard)
        }

        _board.value = newBoard
        _isGameOver.value = !canMakeMove(newBoard)
    }

    // Movimiento vertical: transforma las columnas en lugar de las filas
    private fun moveColumns(transform: (IntArray) -> IntArray) {
        val newBoard = Array(4) { IntArray(4) }

        for (j in 0..3) {
            val col = IntArray(4) { i -> _board.value[i][j] }     // extraer columna
            val newCol = transform(col)                           // transformar columna
            for (i in 0..3) {
                newBoard[i][j] = newCol[i]                        // aplicar cambios
            }
        }

        if (!boardEquals(newBoard, _board.value)) {
            addRandomTile(newBoard)
        }

        _board.value = newBoard
        _isGameOver.value = !canMakeMove(newBoard)
    }

    // Combina y compacta los valores de una fila o columna
    private fun merge(row: IntArray): IntArray {
        val newRow = row.filter { it != 0 }.toMutableList() // elimina ceros
        var i = 0
        var scoreGained = 0

        // Fusiona casillas iguales
        while (i < newRow.size - 1) {
            if (newRow[i] == newRow[i + 1]) {
                newRow[i] *= 2
                scoreGained += newRow[i]
                newRow.removeAt(i + 1)
            }
            i++
        }

        _score.value += scoreGained

        // Devuelve la fila con tamaño fijo de 4, rellenando con ceros
        return newRow.toIntArray().copyOf(4)
    }

    // Compara dos tableros para saber si han cambiado
    private fun boardEquals(a: Array<IntArray>, b: Array<IntArray>): Boolean {
        for (i in 0..3) for (j in 0..3) if (a[i][j] != b[i][j]) return false
        return true
    }

    // Comprueba si todavía es posible hacer algún movimiento
    private fun canMakeMove(board: Array<IntArray>): Boolean {
        for (i in 0..3) {
            for (j in 0..3) {
                if (board[i][j] == 0) return true // hay huecos
                if (j < 3 && board[i][j] == board[i][j + 1]) return true // derecha
                if (i < 3 && board[i][j] == board[i + 1][j]) return true // abajo
            }
        }
        return false // no hay movimientos posibles
    }

}
