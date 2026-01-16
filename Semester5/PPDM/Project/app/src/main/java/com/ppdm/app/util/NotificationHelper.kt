package com.ppdm.app.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationHelper(private val context: Context) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_ID = "ppdm_notifications"
        const val SYNC_ID = 1
        const val STATUS_ID = 2
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "App Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for Sync and Network Status"
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showSyncNotification(count: Int) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_upload_done) // System icon
            .setContentTitle("Sync Complete")
            .setContentText("Successfully uploaded $count offline project(s).")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notify(SYNC_ID, notification)
    }

    fun showNetworkStatusNotification(isOnline: Boolean) {
        val title = if (isOnline) "You are Online" else "You are Offline"
        val text = if (isOnline) "Connection restored. Syncing data..." else "No internet connection. Changes will be saved locally."
        val icon = if (isOnline) android.R.drawable.stat_sys_data_bluetooth else android.R.drawable.stat_notify_error

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_LOW) // Low priority so it doesn't make sound every time
            .setAutoCancel(true)
            .build()

        notify(STATUS_ID, notification)
    }

    private fun notify(id: Int, notification: android.app.Notification) {
        try {
            notificationManager.notify(id, notification)
        } catch (e: SecurityException) {
            // Permission check is handled in UI usually, fail silently in background if revoked
            e.printStackTrace()
        }
    }
}