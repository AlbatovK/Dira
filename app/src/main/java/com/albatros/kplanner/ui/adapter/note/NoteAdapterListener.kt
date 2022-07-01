package com.albatros.kplanner.ui.adapter.note

import android.view.View
import com.albatros.kplanner.model.data.DiraNote

interface NoteAdapterListener {
    fun onNoteSelected(note: DiraNote, view: View)
}