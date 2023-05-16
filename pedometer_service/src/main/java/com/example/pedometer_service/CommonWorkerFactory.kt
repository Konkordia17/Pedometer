package com.example.pedometer_service

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

class WorkerFactoryProvider @Inject constructor(
    private val resetStepCounterWorkerFactory: ResetStepCounterWorkerFactory
) : Provider<Map<Class<out Worker>, ChildWorkerFactory>> {
    override fun get(): Map<Class<out Worker>, ChildWorkerFactory> {
        return mapOf(
            ResetStepCounterWorker::class.java to resetStepCounterWorkerFactory
        )
    }
}

class CommonWorkerFactory @Inject constructor(
    private val resetStepCounterWorkerFactory: ResetStepCounterWorkerFactory
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return mapOf(
            ResetStepCounterWorker::class.java to resetStepCounterWorkerFactory
        ).toList()
            .firstOrNull { it.first.name == workerClassName }?.second?.create(
                appContext = appContext,
                params = workerParameters
            )
    }
}