package com.example.database.di

import android.content.Context
import com.example.database.PedometerDatabase
import dagger.Module
import dagger.Provides

@Module
class DataBaseModule(private val context: Context) {

    @Provides
    fun provideDataBase(context: Context): PedometerDatabase {
        return PedometerDatabase.getInstance(context)
    }

    @Provides
    fun provideContext(): Context = context
}