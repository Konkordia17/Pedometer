package com.example.pedometer.di

import com.example.max_steps_list.Screens
import com.example.pedometer.Screen
import com.example.pedometer_screen.di.PedometerScreen
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NavigationModule {
    private val cicerone: Cicerone<Router> = Cicerone.create()

    @Provides
    @Singleton
    fun provideRouter(): Router {
        return cicerone.router
    }

    @Provides
    @Singleton
    fun provideNavigatorHolder(): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }

    @Provides
    @Singleton
    fun provideScreen(): Screen {
        return Screen()
    }

    @Provides
    @Singleton
    fun provideScreens(): Screens {
        return Screen()
    }

    @Provides
    @Singleton
    fun providePedometerScreen(): PedometerScreen {
        return Screen()
    }

}