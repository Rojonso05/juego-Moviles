package com.example.practica_1_moviles.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {

    // Si ya existe la fila con id = 1, la reemplaza
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(settings: SettingsEntity)

    // Devuelve null mientras el usuario no haya guardado nada
    @Query("SELECT * FROM settings WHERE id = 1")
    fun observe(): Flow<SettingsEntity?>

    @Query("SELECT * FROM settings WHERE id = 1")
    suspend fun get(): SettingsEntity?
}