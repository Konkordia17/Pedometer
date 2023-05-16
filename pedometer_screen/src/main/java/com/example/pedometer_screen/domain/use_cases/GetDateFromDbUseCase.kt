package com.example.pedometer_screen.domain.use_cases

import com.example.pedometer_screen.domain.models.PedometerModel
import com.example.pedometer_screen.domain.reposiory.StepsRepository
import javax.inject.Inject

class GetDateFromDbUseCase @Inject constructor(private val repository: StepsRepository) {

    suspend fun getDataFromDb(): List<PedometerModel> {
        return repository.getData()

    }
}