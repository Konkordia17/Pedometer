package com.example.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.database.PedometerDatabase.Companion.DB_VERSION
import com.example.database.model.PedometerData

@Database(entities = [PedometerData::class], version = DB_VERSION, exportSchema = false)
abstract class PedometerDatabase : RoomDatabase() {

    abstract fun pedometerDao(): PedometerDao

    companion object {
        const val DB_VERSION = 1

        @Volatile
        private var INSTANCE: PedometerDatabase? = null

        fun getInstance(context: Context): PedometerDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                PedometerDatabase::class.java, "PedometerDb"
            ).fallbackToDestructiveMigration().build()
    }
}