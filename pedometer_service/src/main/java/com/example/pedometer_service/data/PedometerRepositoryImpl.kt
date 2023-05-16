package com.example.pedometer_service.data

import com.example.database.PedometerDatabase
import com.example.database.model.PedometerData
import com.example.pedometer_service.domain.repository.PedometerRepository
import com.example.pedometer_service.models.PedometerModel
import javax.inject.Inject

class PedometerRepositoryImpl @Inject constructor(
    private val db: PedometerDatabase
) : PedometerRepository {

    override suspend fun saveData(data: PedometerModel) {
        db.pedometerDao().insertPedometerData(mapPedometerModelToPedometerData(data))
    }

    private fun mapPedometerModelToPedometerData(pedometerModel: PedometerModel): PedometerData {
        return PedometerData(
            date = pedometerModel.date,
            savaTime = pedometerModel.saveTime,
            stepsCount = pedometerModel.stepsCount
        )
    }
}