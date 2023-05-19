package com.example.pedometer_service

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.core.domain.use_cases.GetStepsCounterUseCase
import com.example.pedometer_service.domain.use_cases.SaveDataToDbUseCase
import com.example.pedometer_service.models.PedometerModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class ResetStepCounterWorker(
    val context: Context,
    workerParams: WorkerParameters,
    private val saveDataToDbUseCase: SaveDataToDbUseCase,
    private val getStepsCounterUseCase: GetStepsCounterUseCase
) : Worker(context, workerParams) {

    private val sharedPreferences = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE)

    override fun doWork(): Result {
        return try {
            Log.d("TAGF", "doWork")
            saveData()
            Result.success()
        } catch (e: Throwable) {
            Log.d("TAGF", "doWork  failure")
            Result.failure()
        }
    }

    private fun saveData() {
        val dateModel = getDate()
        val stepsCount = sharedPreferences.getInt(PREVIOUS_STEPS_TAG, 0)
        saveDataToDbUseCase.saveData(
            PedometerModel(
                date = dateModel.date,
                stepsCount = stepsCount,
                saveTime = dateModel.time
            )
        )
        getStepsCounterUseCase.isUpdatedCounts(true)
        resetStepCounter()
    }

    private fun getDate(): DateModel {
        val currentDate = Calendar.getInstance()
        currentDate.add(Calendar.DAY_OF_MONTH, -1)
        val yesterday = currentDate.time
        val formatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        val formattedDate = formatter.format(yesterday)
        val dateFormat = SimpleDateFormat(TIME_FORMAT, Locale.getDefault())
        val formattedTime = dateFormat.format(yesterday)
        return DateModel(date = formattedDate, time = formattedTime)

    }

    override fun onStopped() {
        super.onStopped()
        saveDataToDbUseCase.cancelScope()
    }

    private fun resetStepCounter() {
        sharedPreferences.edit().putInt(PREVIOUS_STEPS_TAG, 0)
            .apply()
        getStepsCounterUseCase.setStepsToCountSubject(0)
    }

    private companion object {
        private const val PREFS_TAG = "prefs"
        private const val DATE_FORMAT = "dd.MM.yyyy"
        private const val TIME_FORMAT = "HH часов mm минут ss секунд"
        private const val PREVIOUS_STEPS_TAG = "previousSteps"
    }
}

data class DateModel(
    val date: String,
    val time: String
)
