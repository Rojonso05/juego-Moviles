package com.example.practica_1_moviles.ui.ranking

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica_1_moviles.data.QuestionDatabase
import com.example.practica_1_moviles.data.ScoreEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RankingViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = QuestionDatabase.getDatabase(application).scoreDao()

    // Top 10 como StateFlow, igual que las temáticas
    val scores: StateFlow<List<ScoreEntity>> = dao.getTopScores()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
        )

    fun delete(score: ScoreEntity) {
        viewModelScope.launch { dao.delete(score) }
    }

    fun deleteAll() {
        viewModelScope.launch { dao.deleteAll() }
    }
}