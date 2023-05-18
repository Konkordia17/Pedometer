package com.example.max_steps_list.di

import com.example.max_steps_list.Screens
import com.github.terrakok.cicerone.Router

interface MaxStepsListComponentDependencies {

    fun getRouter(): Router

    fun getScreens(): Screens
}