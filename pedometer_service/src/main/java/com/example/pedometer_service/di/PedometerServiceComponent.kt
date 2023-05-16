package com.example.pedometer_service.di

import com.example.database.di.DataBaseModule
import com.example.pedometer_service.PedometerService
import dagger.Component

@Component(
    modules = [DataBaseModule::class, PedometerModule::class],
    dependencies = [PedometerServiceComponentDependencies::class]
)
interface PedometerServiceComponent {

    fun injectPedometerService(service: PedometerService)
}