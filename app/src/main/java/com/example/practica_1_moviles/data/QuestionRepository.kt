package com.example.practica_1_moviles.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class QuestionRepository(private val dao: QuestionDao) {

    companion object {
        // Temática especial: mezcla todas las preguntas
        const val RANDOM = "Al azar"

        private val seedMutex = Mutex()
        private var seeded = false

        fun create(context: Context) =
            QuestionRepository(QuestionDatabase.getDatabase(context).questionDao())
    }

    // Carga las preguntas iniciales una sola vez (y solo si la tabla está vacía)
    private suspend fun ensureSeeded() {
        seedMutex.withLock {
            if (!seeded) {
                seedIfEmpty(dao)
                seeded = true
            }
        }
    }

    suspend fun getQuestions(category: String): List<Question> {
        ensureSeeded()
        val rows = if (category == RANDOM) {
            dao.getAllRandom()
        } else {
            dao.getByCategory(category).ifEmpty { dao.getAllRandom() }
        }
        return rows.map { it.toQuestion() }
    }

    // Temáticas disponibles (para la pantalla de selección del siguiente paso)
    fun getCategories(): Flow<List<String>> = flow {
        ensureSeeded()
        emitAll(dao.getCategories())
    }
}