package com.albatros.kplanner.model.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.albatros.kplanner.R

class AlarmReceiver : BroadcastReceiver() {

    private var chnId = "DR_CH"
    private var chnName = "DR_Main_Thread"
    private var channel: NotificationChannel? = null

    private fun createCurrentChannel(manager: NotificationManagerCompat) {
        channel = NotificationChannel(chnId, chnName, NotificationManager.IMPORTANCE_HIGH)
        channel?.enableVibration(true)
        try { channel?.let { manager.createNotificationChannel(it) } }
        catch (ignored: Exception) {}
    }

    private fun getNotification(context: Context, header: String, content: String): Notification {
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, chnId)
                .setContentText(content)
                .setContentTitle(header)
                .setSmallIcon(R.drawable.logo)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
        return builder.setChannelId(chnId).build()
    }


    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            intent?.let {
                val manager = NotificationManagerCompat.from(context)
                createCurrentChannel(manager)
                val header = intent.getStringExtra("EXT_TITLE") ?: return
                val content = intent.getStringExtra("EXT_DESCRIPTION") ?: return
                val notification = getNotification(context, header, content)
                manager.notify(1, notification)
            }
        }
    }
}