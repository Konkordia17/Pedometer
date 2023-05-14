package com.example.pedometer

import com.example.pedometer_screen.PedometerFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

class Screens {

    fun pedometerFragment() = FragmentScreen {
        PedometerFragment.newInstance()
    }
}