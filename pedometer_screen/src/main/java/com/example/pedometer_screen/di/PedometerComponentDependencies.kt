package com.example.pedometer_screen.di

import com.example.core.domain.use_cases.GetStepsCounterUseCase

interface PedometerComponentDependencies {

    fun getUseCase(): GetStepsCounterUseCase
}