package com.example.pedometer_service

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.core.domain.use_cases.GetStepsCounterUseCase
import com.example.pedometer_service.domain.use_cases.SaveDataToDbUseCase
import com.example.pedometer_service.models.PedometerModel
import java.text.SimpleDateFormat
import java.util.Date


class ResetStepCounterWorker(
    val context: Context,
    workerParams: WorkerParameters,
    private val saveDataToDbUseCase: SaveDataToDbUseCase,
    private val getStepsCounterUseCase: GetStepsCounterUseCase
) : Worker(context, workerParams) {

    private val sharedPreferences = context.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE)

    override fun doWork(): Result {
        return try {
            resetStepCounter()
            saveData()
            Result.success()
        } catch (e: Throwable) {

            Result.failure()
        }
    }

    private fun saveData() {
        val dateModel = getDate()
        val stepsCount = sharedPreferences.getInt(TOTAL_STEPS_TAG, 0)
        saveDataToDbUseCase.saveData(
            PedometerModel(
                date = dateModel.date,
                stepsCount = stepsCount,
                saveTime = dateModel.time
            )
        )
        getStepsCounterUseCase.isUpdatedCounts(true)
    }

    private fun getDate(): DateModel {
        val currentDate = Date()
        val formatter = SimpleDateFormat(DATE_FORMAT)
        val formattedDate = formatter.format(currentDate)

        val dateFormat = SimpleDateFormat(TIME_FORMAT)
        val currentTime = Date()
        val formattedTime = dateFormat.format(currentTime)

        return DateModel(date = formattedDate, time = formattedTime)

    }

    private fun resetStepCounter() {
        sharedPreferences.edit().putInt(PREVIOUS_STEPS_TAG, 0)
            .apply()
        getStepsCounterUseCase.setStepsToCountSubject(0)
    }

    private companion object {
        private const val PREFS_TAG = "prefs"
        private const val TOTAL_STEPS_TAG = "totalSteps"
        private const val DATE_FORMAT = "dd.MM.yyyy"
        private const val TIME_FORMAT = "HH часов mm минут ss секунд"
        private const val PREVIOUS_STEPS_TAG = "previousSteps"
    }
}

data class DateModel(
    val date: String,
    val time: String
)
