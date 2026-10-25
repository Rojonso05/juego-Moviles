package com.example.practica_1_moviles.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [QuestionEntity::class, ScoreEntity::class, SettingsEntity::class],
    version = 5,
    exportSchema = false
)
abstract class QuestionDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao

    abstract fun scoreDao(): ScoreDao


    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var Instance: QuestionDatabase? = null

        // Una única instancia para toda la app (Singleton)
        fun getDatabase(context: Context): QuestionDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    QuestionDatabase::class.java,
                    "question_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}