package com.example.pedometer

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACTIVITY_RECOGNITION
import android.Manifest.permission.POST_NOTIFICATIONS
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.pedometer_service.PedometerService
import com.example.pedometer_service.PedometerService.Companion.NOTIFICATION_INTENT_NAME
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router
    private val navigator = AppNavigator(this, R.id.container)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val isBackgroundLocationGranted =
                permissions[ACCESS_BACKGROUND_LOCATION] ?: false
            val isActivityRecognitionGranted =
                permissions[ACTIVITY_RECOGNITION] ?: false

            if (isBackgroundLocationGranted && isActivityRecognitionGranted) {
                val intent = Intent(INTENT_ACTION)
                this.sendBroadcast(intent)
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.need_accept_permission),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == PedometerService.NOTIFICATION_ACTION) {
                val steps = intent.getIntExtra(NOTIFICATION_INTENT_NAME, 0)
                router.replaceScreen(screen = Screen().pedometerFragment(steps))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        (application as App).appComponent.injectMainActivity(this)
        navigatorHolder.setNavigator(navigator)
        sharedPreferences = this.getSharedPreferences(PREFS_TAG, Context.MODE_PRIVATE)
        openStartScreen()
        requestPermissions()

        val filter = IntentFilter()
        filter.addAction(PedometerService.NOTIFICATION_ACTION)
        registerReceiver(receiver, filter)
    }

    private fun openStartScreen() {
        val maxSteps = getChosenMaxSteps()
        if (maxSteps == 0) {
            router.replaceScreen(Screen().maxStepsFragment())
        } else {
            router.replaceScreen(screen = Screen().pedometerFragment(maxSteps))
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val permissionsToRequest = arrayOf(
                ACCESS_BACKGROUND_LOCATION,
                ACTIVITY_RECOGNITION,
                POST_NOTIFICATIONS
            )
            val permissionsNotGranted = mutableListOf<String>()

            for (permission in permissionsToRequest) {
                if (ContextCompat.checkSelfPermission(
                        this, permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionsNotGranted.add(permission)
                }
            }

            if (permissionsNotGranted.isNotEmpty()) {
                requestPermissionLauncher.launch(permissionsNotGranted.toTypedArray())
            } else {
                val intent = Intent(INTENT_ACTION)
                this.sendBroadcast(intent)
            }
        }
    }

    private fun getChosenMaxSteps(): Int {
        return sharedPreferences.getInt(CHOSEN_MAX_STEPS, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    companion object {
        private const val INTENT_ACTION = "ACTION_CUSTOM_BROADCAST"
        private const val PREFS_TAG = "prefs"
        private const val CHOSEN_MAX_STEPS = "chosen_steps"
    }
}