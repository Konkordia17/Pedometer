package com.example.pedometer

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.database.di.DataBaseModule
import com.example.pedometer.di.AppComponent
import com.example.pedometer.di.DaggerAppComponent
import com.example.pedometer.di.StepsCountModule
import com.example.pedometer_screen.di.PedometerComponentDependencies
import com.example.pedometer_screen.di.PedometerComponentDependenciesProvider
import com.example.pedometer_service.CommonWorkerFactory
import com.example.pedometer_service.di.PedometerServiceComponentDependencies
import com.example.pedometer_service.di.PedometerServiceComponentDependenciesProvider
import com.example.pedometer_service.domain.use_cases.InitializeResetStepCounterWorkerUseCase
import javax.inject.Inject

class App : Application(), PedometerComponentDependenciesProvider,
    PedometerServiceComponentDependenciesProvider {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var workerFactory: CommonWorkerFactory

    @Inject
    lateinit var useCase: InitializeResetStepCounterWorkerUseCase

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .stepsCountModule(StepsCountModule(this))
            .dataBaseModule(DataBaseModule(this))
            .build()

        appComponent.inject(this)

        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()
        )
    }

    override fun getPedometerComponentDependencies(): PedometerComponentDependencies {
        return appComponent
    }

    override fun getPedometerServiceComponentDependencies(): PedometerServiceComponentDependencies {
        return appComponent
    }
}