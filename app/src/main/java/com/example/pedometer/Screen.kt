package com.example.pedometer

import com.example.max_steps_list.Screens
import com.example.max_steps_list.presentation.MaxStepsListFragment
import com.example.pedometer_screen.PedometerFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

class Screen : Screens{
    override fun pedometerFragment(steps: Int) = FragmentScreen {
        PedometerFragment.newInstance(steps)
    }

    fun maxStepsFragment() = FragmentScreen {
        MaxStepsListFragment.newInstance()
    }
}