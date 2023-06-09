package com.example.core.domain.use_cases

import kotlinx.coroutines.flow.SharedFlow

interface GetStepsCounterUseCase {

    fun getStepsCount(): SharedFlow<Int>

    fun setStepsCount(count: Int)

    fun getUpdateFlow(): SharedFlow<Boolean>
    fun isUpdatedCounts(isUpdated: Boolean)

    fun getUpdatedMaxSteps(): SharedFlow<Int>
    fun setMaxSteps(maxSteps: Int)
}