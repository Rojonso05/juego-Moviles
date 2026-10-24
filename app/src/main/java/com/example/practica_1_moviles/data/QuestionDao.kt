package com.example.practica_1_moviles.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(question: QuestionEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(questions: List<QuestionEntity>)

    // Preguntas de una temática, ya mezcladas
    @Query("SELECT * FROM questions WHERE category = :category ORDER BY RANDOM()")
    suspend fun getByCategory(category: String): List<QuestionEntity>

    // Todas las preguntas mezcladas (temática "Al azar")
    @Query("SELECT * FROM questions ORDER BY RANDOM()")
    suspend fun getAllRandom(): List<QuestionEntity>

    // Temáticas que existen en la base de datos (para generar los botones)
    @Query("SELECT DISTINCT category FROM questions ORDER BY category")
    fun getCategories(): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun count(): Int

    @Query("DELETE FROM questions")
    suspend fun deleteAll()
}