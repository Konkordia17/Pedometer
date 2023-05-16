package com.example.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.database.model.PedometerData

@Dao
interface PedometerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPedometerData(data: PedometerData)

    @Query("SELECT * FROM pedometerData")
    suspend fun getPedometerData(): List<PedometerData>
}