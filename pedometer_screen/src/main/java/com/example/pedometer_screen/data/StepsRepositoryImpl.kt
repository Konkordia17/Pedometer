package com.example.pedometer_screen.data

import com.example.database.PedometerDatabase
import com.example.database.model.PedometerData
import com.example.pedometer_screen.domain.models.PedometerModel
import com.example.pedometer_screen.domain.reposiory.StepsRepository
import javax.inject.Inject

class StepsRepositoryImpl @Inject constructor(private val db: PedometerDatabase) : StepsRepository {

    override suspend fun getData(): List<PedometerModel> {
        return db.pedometerDao().getPedometerData()
            .map {
                mapPedometerDataToPedometerModel(it)
            }
    }

    private fun mapPedometerDataToPedometerModel(pedometerData: PedometerData): PedometerModel {
        return PedometerModel(
            date = pedometerData.date,
            saveTime = pedometerData.savaTime,
            stepsCount = pedometerData.stepsCount
        )
    }
}