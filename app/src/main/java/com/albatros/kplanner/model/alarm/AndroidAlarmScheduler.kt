package com.albatros.kplanner.model.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.albatros.kplanner.model.data.DiraNote

class AndroidAlarmScheduler(private val context: Context) : AlarmScheduler {

    private val manager by lazy {
        context.getSystemService(AlarmManager::class.java)
    }

    override fun schedule(item: DiraNote) {

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("EXT_TITLE", item.title)
            putExtra("EXT_DESCRIPTION", item.description)
        }

        manager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.time,
            PendingIntent.getBroadcast(
                context,
                item.id.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(item: DiraNote) {
        manager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.id.toInt(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}