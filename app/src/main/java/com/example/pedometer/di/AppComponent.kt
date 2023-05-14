package com.example.pedometer.di

import com.example.core.domain.use_cases.GetStepsCounterUseCase
import com.example.pedometer.MainActivity
import com.example.pedometer.domain.use_cases.GetStepsCountUseCaseImpl
import com.example.pedometer_screen.di.PedometerComponentDependencies
import com.example.pedometer_service.di.PedometerServiceComponentDependencies

import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [NavigationModule::class, StepsCountModule::class])
interface AppComponent : PedometerComponentDependencies, PedometerServiceComponentDependencies {

    fun injectMainActivity(mainActivity: MainActivity)

    override fun getUseCase(): GetStepsCounterUseCase {
        return GetStepsCountUseCaseImpl()
    }

    override fun getServiceStepsUseCase(): GetStepsCounterUseCase {
        return GetStepsCountUseCaseImpl()
    }
}