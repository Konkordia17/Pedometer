package com.example.pedometer_screen.di

import com.example.database.PedometerDatabase
import com.example.pedometer_screen.data.StepsRepositoryImpl
import com.example.pedometer_screen.domain.reposiory.StepsRepository
import dagger.Module
import dagger.Provides

@Module
class StepsModule {

    @Provides
    fun provideStepsRepository(db: PedometerDatabase): StepsRepository {
        return StepsRepositoryImpl(db)
    }
}