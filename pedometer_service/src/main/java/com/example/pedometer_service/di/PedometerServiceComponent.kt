package com.example.pedometer_service.di

import com.example.pedometer_service.PedometerService
import dagger.Component

@Component(dependencies = [PedometerServiceComponentDependencies::class])
interface PedometerServiceComponent {

    fun injectPedometerService(service: PedometerService)
}