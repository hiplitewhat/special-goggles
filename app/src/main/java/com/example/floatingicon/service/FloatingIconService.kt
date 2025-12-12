package com.example.floatingicon.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.floatingicon.R

class FloatingIconService : Service() {

    private lateinit var floatingIconManager: FloatingIconManager
    private val notificationId = 1

    override fun onCreate() {
        super.onCreate()
        floatingIconManager = FloatingIconManager(this)
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        floatingIconManager.showFloatingIcon()
        startForeground(notificationId, createNotification())
        return START_STICKY
    }

    override fun onDestroy() {
        floatingIconManager.hideFloatingIcon()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Floating Icon Service",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notification for floating icon overlay service"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "floating_icon_channel"
    }
}
