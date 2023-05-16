package com.example.pedometer_service

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.core.domain.use_cases.GetStepsCounterUseCase
import com.example.pedometer_service.domain.use_cases.SaveDataToDbUseCase
import javax.inject.Inject

class ResetStepCounterWorkerFactory @Inject constructor(
    private val saveDataToDbUseCase: SaveDataToDbUseCase,
    private val getStepsCounterUseCase: GetStepsCounterUseCase
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): Worker {
        return ResetStepCounterWorker(
            appContext,
            params,
            saveDataToDbUseCase,
            getStepsCounterUseCase
        )
    }
}