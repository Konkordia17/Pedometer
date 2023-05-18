package com.example.max_steps_list.di

import com.example.max_steps_list.presentation.MaxStepsListFragment
import dagger.Component

@Component(dependencies = [MaxStepsListComponentDependencies::class])
interface MaxStepsListComponent {

    fun injectMaxListFragment(maxStepsListFragment: MaxStepsListFragment)

}