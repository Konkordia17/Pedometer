package com.example.core.domain.use_cases

import io.reactivex.subjects.PublishSubject

interface GetStepsCounterUseCase {

    fun getCountSubject(): PublishSubject<Int>

    fun setStepsToCountSubject(count: Int)
}