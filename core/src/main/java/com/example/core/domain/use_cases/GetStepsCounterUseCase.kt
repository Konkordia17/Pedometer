package com.example.core.domain.use_cases

import io.reactivex.subjects.PublishSubject

interface GetStepsCounterUseCase {

    fun getCountSubject(): PublishSubject<Int>

    fun setStepsToCountSubject(count: Int)

    fun getUpdateSubject(): PublishSubject<Boolean>
    fun isUpdatedCounts(isUpdated: Boolean)

    fun getUpdatedMaxStepsSubject(): PublishSubject<Int>
    fun setMaxSteps(maxSteps: Int)
}