package com.albatros.kplanner.ui.adapter.note

import androidx.cardview.widget.CardView
import com.albatros.kplanner.model.data.DiraNote

interface NoteAdapterListener {
    fun onNoteSelected(note: DiraNote, view: CardView)
    fun onNoteClicked(note: DiraNote, view: CardView)
}