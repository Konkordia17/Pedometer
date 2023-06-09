package com.example.pedometer.domain.use_cases

import com.example.core.domain.use_cases.GetStepsCounterUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow


class GetStepsCountUseCaseImpl : GetStepsCounterUseCase {
    private val _counterFlow = MutableStateFlow(0)
    private val _isUpdatedCounterFlow = MutableStateFlow(false)
    private val _updatedMaxSteps = MutableStateFlow(0)

    override fun getStepsCount(): SharedFlow<Int> {
        return _counterFlow.asSharedFlow()
    }

    override fun setStepsCount(count: Int) {
        _counterFlow.value = count
    }

    override fun getUpdateFlow(): SharedFlow<Boolean> {
        return _isUpdatedCounterFlow.asSharedFlow()
    }

    override fun isUpdatedCounts(isUpdated: Boolean) {
        _isUpdatedCounterFlow.value = isUpdated
    }

    override fun getUpdatedMaxSteps(): SharedFlow<Int> {
        return _updatedMaxSteps.asSharedFlow()
    }

    override fun setMaxSteps(maxSteps: Int) {
       _updatedMaxSteps.value = maxSteps
    }
}