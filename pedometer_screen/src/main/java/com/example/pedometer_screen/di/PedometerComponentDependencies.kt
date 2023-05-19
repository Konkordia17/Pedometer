package com.example.pedometer_screen.di

import com.example.core.domain.use_cases.GetStepsCounterUseCase
import com.github.terrakok.cicerone.Router

interface PedometerComponentDependencies {

    fun getUseCase(): GetStepsCounterUseCase

    fun getPedometerRouter(): Router

    fun getPedometerScreens(): PedometerScreen
}