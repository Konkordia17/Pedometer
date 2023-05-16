package com.example.pedometer_service.domain.use_cases


interface InitializeResetStepCounterWorkerUseCase {

    fun initializeResetStepCounter()

    fun cancel()
}