package com.example.practica_1_moviles.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,          // cultura general, monstruos, videojuegos, al azar
    val text: String,
    val answer1: String,
    val answer2: String,
    val answer3: String,
    val answer4: String,
    val correctIndex: Int,         // 0 a 3
    val timeLimitMs: Long = 10_000L
) {
    // Convierte la fila de la base de datos en la Question que ya usa el juego
    fun toQuestion(): Question {
        val answers = listOf(answer1, answer2, answer3, answer4)
        val correct = answers[correctIndex]
        val shuffled = answers.shuffled()
        return Question(
            text = text,
            answers = shuffled,
            correctIndex = shuffled.indexOf(correct),
            timeLimitMs = timeLimitMs
        )
    }
}