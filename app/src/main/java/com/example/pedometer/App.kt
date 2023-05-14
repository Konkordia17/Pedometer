package com.example.pedometer

import android.app.Application
import com.example.pedometer.di.AppComponent
import com.example.pedometer.di.DaggerAppComponent
import com.example.pedometer_screen.di.PedometerComponentDependencies
import com.example.pedometer_screen.di.PedometerComponentDependenciesProvider
import com.example.pedometer_service.di.PedometerServiceComponentDependencies
import com.example.pedometer_service.di.PedometerServiceComponentDependenciesProvider

class App : Application(), PedometerComponentDependenciesProvider,
    PedometerServiceComponentDependenciesProvider {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.create()

    }

    override fun getPedometerComponentDependencies(): PedometerComponentDependencies {
        return appComponent
    }

    override fun getPedometerServiceComponentDependencies(): PedometerServiceComponentDependencies {
        return appComponent
    }
}