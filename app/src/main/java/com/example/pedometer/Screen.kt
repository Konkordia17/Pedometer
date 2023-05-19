package com.example.pedometer

import com.example.max_steps_list.Screens
import com.example.max_steps_list.presentation.MaxStepsListFragment
import com.example.pedometer_screen.PedometerFragment
import com.example.pedometer_screen.di.PedometerScreen
import com.example.statistic_fragment.StatisticFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

class Screen : Screens, PedometerScreen {
    override fun pedometerFragment(steps: Int) = FragmentScreen {
        PedometerFragment.newInstance(steps)
    }

    fun maxStepsFragment() = FragmentScreen {
        MaxStepsListFragment.newInstance()
    }

    override fun changeMaxSteps() = FragmentScreen {
        MaxStepsListFragment.newInstance()
    }

    override fun openStatisticScreen() = FragmentScreen {
       StatisticFragment()
    }
}