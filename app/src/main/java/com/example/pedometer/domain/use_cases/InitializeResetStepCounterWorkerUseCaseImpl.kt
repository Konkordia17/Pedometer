package com.example.pedometer.domain.use_cases

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.pedometer_service.ResetStepCounterWorker
import com.example.pedometer_service.domain.use_cases.InitializeResetStepCounterWorkerUseCase
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class InitializeResetStepCounterWorkerUseCaseImpl @Inject constructor(private val context: Context) :
    InitializeResetStepCounterWorkerUseCase {
    override fun initializeResetStepCounter() {
        startResetStepCounterWorker(context)
    }

    private fun startResetStepCounterWorker(context: Context) {
        val myWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequest.Builder(
                ResetStepCounterWorker::class.java,
                24,
                TimeUnit.HOURS
            )
                .setInitialDelay(getTimeBeforeMidnight(), TimeUnit.MILLISECONDS)
                .addTag(WORK_TAG)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            myWorkRequest
        )
    }

    override fun cancel() {
        WorkManager.getInstance(context).cancelAllWorkByTag(WORK_TAG)
    }

    private fun getTimeBeforeMidnight(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, HOURS)
        calendar.set(Calendar.MINUTE, MINUTES_OR_SECONDS_COUNT)
        calendar.set(Calendar.SECOND, MINUTES_OR_SECONDS_COUNT)
        calendar.set(Calendar.MILLISECOND, MILLISECONDS)
        return calendar.timeInMillis - System.currentTimeMillis()
    }


    private companion object {
        private const val WORK_TAG = "tag"
        private const val WORK_UNIQUE_NAME = "MY_UNIQUE_WORK"
        private const val MINUTES_OR_SECONDS_COUNT = 59
        private const val MILLISECONDS = 999
        private const val HOURS = 23
    }
}