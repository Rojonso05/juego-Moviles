package com.example.practica_1_moviles.ui.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica_1_moviles.data.Monster
import com.example.practica_1_moviles.data.Question
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class GameStatus { ANSWERING, FEEDBACK, VICTORY, DEFEAT }

class GameViewModel : ViewModel() {

    companion object {
        const val PLAYER_MAX_HP = 100
        const val MIN_DAMAGE = 10
        const val MAX_DAMAGE = 50
        const val TICK_MS = 100L
    }

    // --- Datos del juego (de ejemplo, los cambiaremos con la temática) ---
    private val monsters = listOf(
        Monster("Slime", maxHp = 60, damage = 15),
        Monster("Goblin", maxHp = 90, damage = 20),
        Monster("Dragón", maxHp = 150, damage = 30)
    )
    private val questions = listOf(
        Question("Pregunta de ejemplo 1 bladaidwbdhwbduwvduywduwdvwd", listOf("A", "B", "C", "D"), 0),
        Question("Pregunta de ejemplo 2", listOf("A", "B", "C", "D"), 2),
        Question("Pregunta de ejemplo 3", listOf("A", "B", "C", "D"), 1)
    ).shuffled()

    // --- Estado observable por la UI ---
    var status by mutableStateOf(GameStatus.ANSWERING)
        private set
    var playerHp by mutableIntStateOf(PLAYER_MAX_HP)
        private set
    var monsterIndex by mutableIntStateOf(0)
        private set
    var monsterHp by mutableIntStateOf(monsters[0].maxHp)
        private set
    var questionIndex by mutableIntStateOf(0)
        private set
    var timeLeftMs by mutableLongStateOf(questions[0].timeLimitMs)
        private set
    var selectedAnswer by mutableStateOf<Int?>(null)
        private set
    var lastDamageDealt by mutableIntStateOf(0)
        private set
    var lastDamageTaken by mutableIntStateOf(0)
        private set

    val currentMonster: Monster get() = monsters[monsterIndex]
    val currentQuestion: Question get() = questions[questionIndex % questions.size]

    private var timerJob: Job? = null

    init { startTimer() }

    // --- Temporizador ---
    private fun startTimer() {
        timerJob?.cancel()
        timeLeftMs = currentQuestion.timeLimitMs
        timerJob = viewModelScope.launch {
            while (timeLeftMs > 0) {
                delay(TICK_MS)
                timeLeftMs -= TICK_MS
            }
            onTimeout()
        }
    }

    // --- Acciones del jugador ---
    fun onAnswerSelected(index: Int) {
        if (status != GameStatus.ANSWERING) return
        timerJob?.cancel()
        selectedAnswer = index

        if (index == currentQuestion.correctIndex) {
            val fraction = timeLeftMs.toFloat() / currentQuestion.timeLimitMs
            val damage = (MIN_DAMAGE + (MAX_DAMAGE - MIN_DAMAGE) * fraction).toInt()
            monsterHp = (monsterHp - damage).coerceAtLeast(0)
            lastDamageDealt = damage
            lastDamageTaken = 0
        } else {
            hitPlayer()
        }
        resolveTurn()
    }

    private fun onTimeout() {
        selectedAnswer = null
        hitPlayer()
        resolveTurn()
    }

    private fun hitPlayer() {
        lastDamageDealt = 0
        lastDamageTaken = currentMonster.damage
        playerHp = (playerHp - lastDamageTaken).coerceAtLeast(0)
    }

    private fun resolveTurn() {
        status = when {
            playerHp <= 0 -> GameStatus.DEFEAT
            monsterHp <= 0 && monsterIndex == monsters.lastIndex -> GameStatus.VICTORY
            else -> GameStatus.FEEDBACK
        }
    }

    fun nextTurn() {
        if (status != GameStatus.FEEDBACK) return
        if (monsterHp <= 0) {
            monsterIndex++
            monsterHp = currentMonster.maxHp
        }
        questionIndex++
        selectedAnswer = null
        status = GameStatus.ANSWERING
        startTimer()
    }
}