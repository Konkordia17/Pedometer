package com.example.pedometer_service.domain.repository

import com.example.pedometer_service.models.PedometerModel

interface PedometerRepository {

    suspend fun saveData(data: PedometerModel)
}