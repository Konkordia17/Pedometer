package com.example.pedometer_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.domain.use_cases.GetStepsCounterUseCase
import com.example.pedometer_screen.domain.models.PedometerModel
import com.example.pedometer_screen.domain.use_cases.GetDateFromDbUseCase
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PedometerViewModel(
    private val useCase: GetStepsCounterUseCase,
    private val getDataBaseUseCase: GetDateFromDbUseCase
) : ViewModel() {

    private val _count = MutableLiveData<Int>()
    val count: LiveData<Int> = _count

    private val _isUpdatedCounter = MutableLiveData<Boolean>()
    val isUpdatedCounter: LiveData<Boolean> = _isUpdatedCounter

    private val _previousSteps = MutableLiveData<PedometerModel>()
    val previousSteps: LiveData<PedometerModel> = _previousSteps

    private var disposable = CompositeDisposable()

    private val handler = CoroutineExceptionHandler { _, exception ->
        println("CoroutineExceptionHandler got $exception")
    }

    init {
        getStepsCount()
        subscribeOnUpdateCounter()
        getDataFromDatabase()
    }

    fun getDataFromDatabase() {
        viewModelScope.launch(handler + Dispatchers.IO) {
            _previousSteps.postValue(
                getDataBaseUseCase.getDataFromDb().last {
                    it.date == getCurrentDate()
                }
            )
        }
    }

    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance()
        currentDate.add(Calendar.DAY_OF_MONTH, -1)
        val yesterday = currentDate.time
        val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return formatter.format(yesterday)
    }

    private fun getStepsCount() {
        disposable.add(useCase.getCountSubject()
            .subscribe {
                _count.postValue(it)
            })
    }

    private fun subscribeOnUpdateCounter() {
        disposable.add(useCase.getUpdateSubject()
            .subscribe {
                _isUpdatedCounter.postValue(it)
            })
    }

    fun updateMaxSteps(maxSteps: Int) {
        useCase.setMaxSteps(maxSteps)
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
        disposable.dispose()
    }

    private companion object {
        private const val DATE_FORMAT = "dd.MM.yyyy"
    }
}