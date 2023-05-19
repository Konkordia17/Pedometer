package com.example.pedometer_screen.di

import com.github.terrakok.cicerone.androidx.FragmentScreen

interface PedometerScreen {

    fun changeMaxSteps(): FragmentScreen

    fun openStatisticScreen(): FragmentScreen

}