package com.jakondev.game2048

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakondev.a2048_game.viewmodel.SampleBoards
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

// ViewModel que gestiona el estado del juego 2048.
class GameViewModel : ViewModel() {

    // Estado del tablero (4x4), representado como una matriz de enteros.
    private val _board = MutableStateFlow(Array(4) { IntArray(4) { 0 } })
    val board = _board.asStateFlow()

    private val _prevBoard = MutableStateFlow(Array(4) { IntArray(4) { 0 } })
    val prevBoard = _prevBoard.asStateFlow()

    // Puntuación actual del jugador.
    private val _score = MutableStateFlow(0)
    val score = _score.asStateFlow()

    // Contador de movimientos.
    private val _swipes = MutableStateFlow(0)
    val swipes = _swipes.asStateFlow()

    // Incrementa el contador de movimientos.
    fun incrementSwipes() {
        _swipes.value++
    }

    // Indica si el juego ha terminado.
    private val _isGameOver = MutableStateFlow(false)
    val isGameOver = _isGameOver.asStateFlow()

    private val _is2048 = MutableStateFlow(false)
    val is2048 = _is2048.asStateFlow()

    private val _hasWon = MutableStateFlow(false)
    val hasWon = _hasWon.asStateFlow()

    private val _time = MutableStateFlow(0) // en segundos
    val time: StateFlow<Int> = _time

    private var isTimeRunning = false
    private var timerJob: Job? = null


    fun startTimer() {
        if (isTimeRunning) return
        isTimeRunning = true
        timerJob = viewModelScope.launch {
            while (isTimeRunning) {
                delay(1000L)
                _time.value += 1
            }
        }
    }

    fun pauseTimer() {
        isTimeRunning = false
        timerJob?.cancel()
    }

    fun resumeTimer() {
        if (!isTimeRunning) {
            startTimer()
        }
    }


    // Reinicia el tablero y la puntuación.
    fun resetGame() {
        val newBoard = SampleBoards().almostWon
        _score.value = 0
        _isGameOver.value = false
        _is2048.value = false
        _hasWon.value = false
        repeat(2) { addRandomTile(newBoard) } // Añade dos fichas al inicio
        _board.value = newBoard
        _prevBoard.value = newBoard
        _swipes.value = 0
        _time.value = 0
        startTimer()
    }

    fun resumeGame() {
        resumeTimer()
        _is2048.value = false
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
        val prevBoard = _board.value
        _prevBoard.value = prevBoard
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
                if (newRow[i] == 2048 && !_hasWon.value) {
                    _is2048.value = true
                    _hasWon.value = true
                }
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

    fun undoMove() {
        if (!boardEquals(_board.value, _prevBoard.value)) {
            _board.value = _prevBoard.value.map { it.clone() }.toTypedArray() // Copia profunda
            _swipes.value = (_swipes.value - 1).coerceAtLeast(0) // No bajar de 0
            _isGameOver.value = false // Si era Game Over, ya no lo será
        }
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
