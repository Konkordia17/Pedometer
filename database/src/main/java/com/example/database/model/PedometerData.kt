package com.example.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PedometerData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val stepsCount: Int,
    val savaTime: String
)