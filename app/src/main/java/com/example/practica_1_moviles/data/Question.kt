package com.example.practica_1_moviles.data

data class Question(
    val text: String,
    val answers: List<String>,
    val correctIndex: Int,
    val timeLimitMs: Long = 10_000L
)