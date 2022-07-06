package com.albatros.kplanner.ui.adapter.schedule

import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.data.Schedule

interface ScheduleAdapterListener {
    fun onNoteFinished(note: DiraNote, schedule: Schedule, view: ImageView)

    fun OnFirstNoteBinded(view: CardView)
}