package com.example.pedometer_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.use_cases.GetStepsCounterUseCase
import javax.inject.Inject

class PedometerViewModelFactory @Inject constructor(
    private val getStepsCountUseCase: GetStepsCounterUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            PedometerViewModel::class.java -> PedometerViewModel(
                getStepsCountUseCase
            )

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    }
}