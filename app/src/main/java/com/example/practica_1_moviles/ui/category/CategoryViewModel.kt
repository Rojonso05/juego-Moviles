package com.example.practica_1_moviles.ui.category

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica_1_moviles.data.QuestionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = QuestionRepository.create(application)

    // Lista de temáticas que hay en la base de datos
    val categories: StateFlow<List<String>> = repository.getCategories()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
        )
}