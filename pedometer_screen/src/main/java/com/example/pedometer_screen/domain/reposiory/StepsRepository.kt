package com.example.pedometer_screen.domain.reposiory

import com.example.pedometer_screen.domain.models.PedometerModel

interface StepsRepository {

    suspend fun getData(): List<PedometerModel>
}