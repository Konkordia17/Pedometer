package com.example.pedometer_service

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.core.domain.use_cases.GetStepsCounterUseCase
import com.example.database.di.DataBaseModule
import com.example.pedometer_service.di.DaggerPedometerServiceComponent
import com.example.pedometer_service.di.PedometerServiceComponent
import com.example.pedometer_service.di.PedometerServiceComponentDependenciesProvider
import com.example.pedometer_service.domain.use_cases.InitializeResetStepCounterWorkerUseCase
import com.example.pedometer_service.domain.use_cases.SaveDataToDbUseCase
import io.reactivex.disposables.Disposable
import javax.inject.Inject


class PedometerService : Service(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var stepSensor: Sensor? = null
    private var currentSteps: Int? = 0
    private var totalStepsCount: Int = 0
    private var isMaxStepsChanged: Boolean = false
    private var chosenStepsCount = 0

    private lateinit var pedometerServiceComponent: PedometerServiceComponent

    @Inject
    lateinit var useCase: GetStepsCounterUseCase

    @Inject
    lateinit var initializeWorkerUseCase: InitializeResetStepCounterWorkerUseCase

    @Inject
    lateinit var saveDataToDbUseCase: SaveDataToDbUseCase

    private lateinit var sharedPreferences: SharedPreferences
    private var disposable: Disposable? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == INTENT_ACTION) {
                registerPedometer()
                initializeWorkerUseCase.initializeResetStepCounter()
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        val filter = IntentFilter()
        filter.addAction(INTENT_ACTION)
        registerReceiver(receiver, filter)
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        registerComponent()
        sharedPreferences = getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE)
        createSensorManager()
        getTotalStepsCount()
        observeMaxStepsChange()
    }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = createNotificationChannel(null)
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            channel?.let { notificationManager.createNotificationChannel(it) }
        }

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.pedometer))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        return notificationBuilder.build()
    }

    private fun registerComponent() {
        val pedometerComponentDependencies =
            (this.applicationContext as PedometerServiceComponentDependenciesProvider)
                .getPedometerServiceComponentDependencies()
        pedometerServiceComponent = DaggerPedometerServiceComponent.builder()
            .pedometerServiceComponentDependencies(pedometerComponentDependencies)
            .dataBaseModule(DataBaseModule(this))
            .build()
        pedometerServiceComponent.injectPedometerService(this)
    }

    private fun createSensorManager() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            showToast(R.string.pedometer_sensor_is_not_available)
            stopSelf()
        }
        if (!isPermissionGranted()) {
            registerPedometer()
            initializeWorkerUseCase.initializeResetStepCounter()
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) != PackageManager.PERMISSION_GRANTED
    }

    private fun registerPedometer() {
        Log.d("TAGF", "registerPedometer")
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            showToast(R.string.pedometer_sensor_is_not_available)
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
            useCase.setStepsToCountSubject(getPreviousStepsCount())
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        Log.d("TAGF", "onSensorChanged  ${event?.sensor}")
        if (stepSensor == event?.sensor) {
            val steps = event?.values?.get(0)?.toInt() ?: 0
            currentSteps = if (steps < totalStepsCount) {
                steps + getPreviousStepsCount()
            } else {
                steps - totalStepsCount + getPreviousStepsCount()
            }
            currentSteps?.let {
                useCase.setStepsToCountSubject(it)
                totalStepsCount = steps
                saveStepsCount(previousSteps = it, totalSteps = totalStepsCount)
                if (it >= chosenStepsCount && it != 0 && isMaxStepsChanged) {
                    createMaxStepsNotification()
                    isMaxStepsChanged = false
                }
            }
        }
    }

    private fun observeMaxStepsChange() {
        disposable = useCase.getUpdatedMaxStepsSubject().subscribe {
            isMaxStepsChanged = chosenStepsCount != it
            chosenStepsCount = it
        }
    }

    private fun createMaxStepsNotification() {
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val channel = createNotificationChannel(soundUri)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_athlete)
            .setContentTitle(this.getString(R.string.hooray))
            .setContentText(this.getString(R.string.achievement_by_steps))
            .setContentIntent(createIntent())
            .setVibrate(longArrayOf(100, 200, 300, 400, 500))
            .setSound(soundUri)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager =
            this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(CHANNEL_ID)
            channel?.let { notificationManager.createNotificationChannel(it) }
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun createNotificationChannel(soundUri: Uri?): NotificationChannel? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            channel.apply {
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            }
            soundUri?.let {
                channel.setSound(
                    soundUri,
                    AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
                )
            }
            channel
        } else {
            null
        }
    }

    private fun createIntent(): PendingIntent {
        val intent = Intent(NOTIFICATION_ACTION)
        intent.putExtra(NOTIFICATION_INTENT_NAME, chosenStepsCount)
        return PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun saveStepsCount(previousSteps: Int, totalSteps: Int) {
        updatePreviousSteps(previousSteps)
        sharedPreferences.edit().putInt(TOTAL_STEPS_TAG, totalSteps)
            .apply()
    }

    private fun updatePreviousSteps(previousSteps: Int) {
        sharedPreferences.edit().putInt(PREVIOUS_STEPS_TAG, previousSteps)
            .apply()
    }

    private fun getTotalStepsCount() {
        totalStepsCount = sharedPreferences.getInt(TOTAL_STEPS_TAG, 0)
    }

    private fun getPreviousStepsCount(): Int {
        return sharedPreferences.getInt(PREVIOUS_STEPS_TAG, 0)
    }


    private fun showToast(id: Int) {
        Toast.makeText(this, resources.getString(id), Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager?.unregisterListener(this)
        initializeWorkerUseCase.cancel()
    }

    companion object {
        const val NOTIFICATION_ACTION = "NOTIFICATION_ACTION"
        const val NOTIFICATION_INTENT_NAME = "notification intent"
        private const val CHANNEL_ID = "channel_id"
        private const val CHANNEL_NAME = "Channel Name"
        private const val NOTIFICATION_ID = 1
        private const val PREFS_TAG = "prefs"
        private const val INTENT_ACTION = "ACTION_CUSTOM_BROADCAST"
        private const val TOTAL_STEPS_TAG = "totalSteps"
        private const val PREVIOUS_STEPS_TAG = "previousSteps"
    }
}
