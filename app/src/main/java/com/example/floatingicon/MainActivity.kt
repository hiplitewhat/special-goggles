package com.example.floatingicon

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.floatingicon.config.FloatingIconConfig
import com.example.floatingicon.databinding.ActivityMainBinding
import com.example.floatingicon.service.FloatingIconService

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var config: FloatingIconConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        config = FloatingIconConfig(this)

        binding.startButton.setOnClickListener {
            if (canDrawOverlays()) {
                startOverlay()
            } else {
                requestOverlayPermission()
            }
        }

        binding.stopButton.setOnClickListener {
            stopOverlay()
        }

        updateUI()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun startOverlay() {
        if (!isServiceRunning(FloatingIconService::class.java)) {
            val intent = Intent(this, FloatingIconService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
            config.isEnabled = true
            updateUI()
        }
    }

    private fun stopOverlay() {
        stopService(Intent(this, FloatingIconService::class.java))
        config.isEnabled = false
        updateUI()
    }

    private fun updateUI() {
        val isRunning = isServiceRunning(FloatingIconService::class.java)
        val statusText = if (isRunning) {
            getString(R.string.overlay_running)
        } else {
            getString(R.string.overlay_stopped)
        }
        binding.statusText.text = statusText
    }

    private fun canDrawOverlays(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else {
            true
        }
    }

    private fun requestOverlayPermission() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("This app needs permission to display an overlay. Please enable it in settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as android.app.ActivityManager
        @Suppress("DEPRECATION")
        return manager.getRunningServices(Integer.MAX_VALUE).any { service ->
            service.service.className == serviceClass.name
        }
    }
}
