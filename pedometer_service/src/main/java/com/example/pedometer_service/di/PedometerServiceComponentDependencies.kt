package com.example.pedometer_service.di

import com.example.core.domain.use_cases.GetStepsCounterUseCase

interface PedometerServiceComponentDependencies {

    fun getServiceStepsUseCase(): GetStepsCounterUseCase
}