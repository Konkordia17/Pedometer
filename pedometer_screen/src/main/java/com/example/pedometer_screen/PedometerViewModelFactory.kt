package com.example.pedometer_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.core.domain.use_cases.GetStepsCounterUseCase
import com.example.pedometer_screen.domain.use_cases.GetDateFromDbUseCase
import javax.inject.Inject

class PedometerViewModelFactory @Inject constructor(
    private val getStepsCountUseCase: GetStepsCounterUseCase,
    private val getDataBaseUseCase: GetDateFromDbUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            PedometerViewModel::class.java -> PedometerViewModel(
                getStepsCountUseCase,
                getDataBaseUseCase
            )

            else -> throw IllegalArgumentException("Unknown ViewModel class")
        } as T
    }
}