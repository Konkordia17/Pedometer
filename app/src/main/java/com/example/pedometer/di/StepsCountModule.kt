package com.example.pedometer.di

import com.example.core.domain.use_cases.GetStepsCounterUseCase
import com.example.pedometer.domain.use_cases.GetStepsCountUseCaseImpl
import com.example.pedometer_service.PedometerService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StepsCountModule {
    @Provides
    @Singleton
    fun provideGetStepsUseCase(): GetStepsCounterUseCase {
        return GetStepsCountUseCaseImpl()
    }

    @Provides
    @Singleton
    fun provideService(): PedometerService {
        return PedometerService()
    }

}