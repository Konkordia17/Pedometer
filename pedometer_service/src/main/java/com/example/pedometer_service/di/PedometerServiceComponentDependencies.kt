package com.example.pedometer_service.di


import com.example.core.domain.use_cases.GetStepsCounterUseCase
import com.example.pedometer_service.domain.use_cases.InitializeResetStepCounterWorkerUseCase

interface PedometerServiceComponentDependencies {

    fun getServiceStepsUseCase(): GetStepsCounterUseCase

    fun getInitializeWorkerUseCase(): InitializeResetStepCounterWorkerUseCase
}