package com.example.pedometer_service.domain.use_cases

import com.example.pedometer_service.domain.repository.PedometerRepository
import com.example.pedometer_service.models.PedometerModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class SaveDataToDbUseCase @Inject constructor(private val repository: PedometerRepository) {
    private val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }
    private val scope = CoroutineScope(Dispatchers.IO)

    fun saveData(data: PedometerModel) {
        scope.launch(handler) {
            repository.saveData(data)
        }
    }

    fun cancelScope() {
        scope.cancel()
    }
}