package com.example.pedometer.di

import android.content.Context
import com.example.core.domain.use_cases.GetStepsCounterUseCase
import com.example.database.di.DataBaseModule
import com.example.max_steps_list.di.MaxStepsListComponentDependencies
import com.example.pedometer.App
import com.example.pedometer.MainActivity
import com.example.pedometer.domain.use_cases.GetStepsCountUseCaseImpl
import com.example.pedometer.domain.use_cases.InitializeResetStepCounterWorkerUseCaseImpl
import com.example.pedometer_screen.di.PedometerComponentDependencies
import com.example.pedometer_service.di.PedometerServiceComponentDependencies
import com.example.pedometer_service.domain.use_cases.InitializeResetStepCounterWorkerUseCase

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NavigationModule::class, StepsCountModule::class, DataBaseModule::class]
)
interface AppComponent : PedometerComponentDependencies, PedometerServiceComponentDependencies, MaxStepsListComponentDependencies {

    fun injectMainActivity(mainActivity: MainActivity)

    fun inject(application: App)

    fun getContext(): Context

    override fun getUseCase(): GetStepsCounterUseCase {
        return GetStepsCountUseCaseImpl()
    }

    override fun getServiceStepsUseCase(): GetStepsCounterUseCase {
        return GetStepsCountUseCaseImpl()
    }

    override fun getInitializeWorkerUseCase(): InitializeResetStepCounterWorkerUseCase {
        return InitializeResetStepCounterWorkerUseCaseImpl(getContext())
    }
}