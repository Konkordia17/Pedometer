package com.example.pedometer.di

import android.content.Context
import androidx.work.WorkerFactory
import com.example.core.domain.use_cases.GetStepsCounterUseCase
import com.example.database.PedometerDatabase
import com.example.pedometer.domain.use_cases.GetStepsCountUseCaseImpl
import com.example.pedometer.domain.use_cases.InitializeResetStepCounterWorkerUseCaseImpl
import com.example.pedometer_service.ChildWorkerFactory
import com.example.pedometer_service.CommonWorkerFactory
import com.example.pedometer_service.PedometerService
import com.example.pedometer_service.ResetStepCounterWorkerFactory
import com.example.pedometer_service.data.PedometerRepositoryImpl
import com.example.pedometer_service.domain.repository.PedometerRepository
import com.example.pedometer_service.domain.use_cases.InitializeResetStepCounterWorkerUseCase
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
class StepsCountModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideGetStepsUseCase(): GetStepsCounterUseCase {
        return GetStepsCountUseCaseImpl()
    }

    @Singleton
    @Provides
    fun providePedometerRepository(db: PedometerDatabase): PedometerRepository {
        return PedometerRepositoryImpl(db)
    }

    @Provides
    @Singleton
    fun provideService(): PedometerService {
        return PedometerService()
    }

    @IntoMap
    @ClassKey(ResetStepCounterWorkerFactory::class)
    @Provides
    fun provideResetStepCounterWorkerFactory(factory: ResetStepCounterWorkerFactory): ChildWorkerFactory {
        return factory
    }

    @Provides
    @Singleton
    fun provideInitializeWorkerUseCase(): InitializeResetStepCounterWorkerUseCase {
        return InitializeResetStepCounterWorkerUseCaseImpl(context)
    }

    @Provides
    @Singleton
    fun provideWorkerFactory(factory: ResetStepCounterWorkerFactory): WorkerFactory {
        return CommonWorkerFactory(
            factory
        )
    }
}
