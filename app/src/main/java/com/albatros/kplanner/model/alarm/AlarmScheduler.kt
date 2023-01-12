package com.albatros.kplanner.model.alarm

import com.albatros.kplanner.model.data.DiraNote

interface AlarmScheduler {
    fun schedule(item: DiraNote)
    fun cancel(item: DiraNote)
}