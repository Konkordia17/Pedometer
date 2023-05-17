package com.example.pedometer

import android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
import android.Manifest.permission.ACTIVITY_RECOGNITION
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.pedometer_service.PedometerService
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as App).appComponent.injectMainActivity(this)
        navigatorHolder.setNavigator(navigator)
        router.replaceScreen(Screens().pedometerFragment())

        val serviceIntent = Intent(this, PedometerService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
        requestPermissions()
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val permissionsToRequest = arrayOf(
                ACCESS_BACKGROUND_LOCATION,
                ACTIVITY_RECOGNITION
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

    companion object {
        private const val INTENT_ACTION = "com.example.ACTION_CUSTOM_BROADCAST"
    }
}