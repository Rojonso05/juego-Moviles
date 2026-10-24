package com.example.practica_1_moviles.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import androidx.room.Delete

@Dao
interface ScoreDao {

    @Insert
    suspend fun insert(score: ScoreEntity)

    // Las 10 mejores puntuaciones. Si hay empate, gana la más antigua.
    @Query("SELECT * FROM scores ORDER BY points DESC, timestamp ASC LIMIT 10")
    fun getTopScores(): Flow<List<ScoreEntity>>
    @Delete
    suspend fun delete(score: ScoreEntity)

    @Query("DELETE FROM scores")
    suspend fun deleteAll()
}