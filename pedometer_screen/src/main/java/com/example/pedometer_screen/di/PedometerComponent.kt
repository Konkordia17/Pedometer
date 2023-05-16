package com.example.pedometer_screen.di

import com.example.database.di.DataBaseModule
import com.example.pedometer_screen.PedometerFragment
import com.example.pedometer_screen.PedometerViewModelFactory
import dagger.Component

@Component(
    modules = [DataBaseModule::class, StepsModule::class],
    dependencies = [PedometerComponentDependencies::class]
)
interface PedometerComponent {

    fun injectPedometerFragment(fragment: PedometerFragment)

    fun getViewModelFactory(): PedometerViewModelFactory

}