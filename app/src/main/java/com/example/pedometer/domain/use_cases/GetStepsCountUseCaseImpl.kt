package com.example.pedometer.domain.use_cases

import com.example.core.domain.use_cases.GetStepsCounterUseCase
import io.reactivex.subjects.PublishSubject


class GetStepsCountUseCaseImpl: GetStepsCounterUseCase  {
    private val counterSubject: PublishSubject<Int> = PublishSubject.create()

    override fun getCountSubject(): PublishSubject<Int> {
        return counterSubject
    }

    override fun setStepsToCountSubject(count: Int) {
        counterSubject.onNext(count)
    }
}