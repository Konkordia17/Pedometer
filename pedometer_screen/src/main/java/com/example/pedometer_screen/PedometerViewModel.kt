package com.example.pedometer_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.core.domain.use_cases.GetStepsCounterUseCase
import io.reactivex.disposables.Disposable

class PedometerViewModel(private val useCase: GetStepsCounterUseCase) : ViewModel() {

    private val _count = MutableLiveData<Int>()
    val count: LiveData<Int> = _count
    private var disposable: Disposable? = null

    init {
        getStepsCount()
    }

    private fun getStepsCount() {
        disposable = useCase.getCountSubject()
            .subscribe {
                _count.postValue(it)
            }
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
        disposable = null
    }
}