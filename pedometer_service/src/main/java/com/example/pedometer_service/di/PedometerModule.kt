package com.example.pedometer_service.di

import com.example.core.domain.use_cases.GetStepsCounterUseCase
import com.example.database.PedometerDatabase
import com.example.pedometer_service.ChildWorkerFactory
import com.example.pedometer_service.ResetStepCounterWorkerFactory
import com.example.pedometer_service.data.PedometerRepositoryImpl
import com.example.pedometer_service.domain.repository.PedometerRepository
import com.example.pedometer_service.domain.use_cases.SaveDataToDbUseCase
import dagger.Module
import dagger.Provides

@Module
class PedometerModule {

    @Provides
    fun providePedometerRepository(db: PedometerDatabase): PedometerRepository {
        return PedometerRepositoryImpl(db)
    }

    @Provides
    fun bindResetStepCounterWorker(
        useCase: SaveDataToDbUseCase,
        getStepsCounterUseCase: GetStepsCounterUseCase
    ): ChildWorkerFactory {
        return ResetStepCounterWorkerFactory(useCase, getStepsCounterUseCase)
    }

    @Provides
    fun provideUseCase(repository: PedometerRepository): SaveDataToDbUseCase {
        return SaveDataToDbUseCase(repository)
    }
}