package com.example.practica_1_moviles.ui.result

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica_1_moviles.data.QuestionDatabase
import com.example.practica_1_moviles.data.ScoreEntity
import kotlinx.coroutines.launch

class ResultViewModel(application: Application) : AndroidViewModel(application) {

    private val scoreDao = QuestionDatabase.getDatabase(application).scoreDao()

    // Evita guardar la misma partida dos veces (también tras rotar la pantalla)
    var saved by mutableStateOf(false)
        private set

    fun saveScore(name: String, points: Int, category: String, victory: Boolean) {
        if (saved) return
        saved = true
        viewModelScope.launch {
            scoreDao.insert(
                ScoreEntity(
                    playerName = name.trim(),
                    points = points,
                    category = category,
                    victory = victory
                )
            )
        }
    }
}