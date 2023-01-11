package com.albatros.kplanner.ui.adapter.schedule

import android.widget.ImageView
import android.widget.TextView
import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.data.Schedule

interface ScheduleAdapterListener {
    fun onNoteFinished(note: DiraNote, schedule: Schedule, view: ImageView)
    fun onTimePicked(note: DiraNote, schedule: Schedule, view: TextView)
    fun onItemMoved(fromPosition: Int, toPosition: Int, adapter: ScheduleAdapter): Boolean
    fun onItemDismiss(position: Int, adapter: ScheduleAdapter)
}