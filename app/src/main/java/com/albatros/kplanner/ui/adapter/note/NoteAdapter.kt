package com.albatros.kplanner.ui.adapter.note

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.albatros.kplanner.databinding.NoteLayoutBinding
import com.albatros.kplanner.domain.playFadeInAnimation
import com.albatros.kplanner.model.data.DiraNote

class NoteAdapter(
    private val notes: MutableList<DiraNote>,
    private val listener: NoteAdapterListener
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun getItemCount(): Int = notes.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) { holder.note = notes[position] }

    inner class NoteViewHolder(private val binding: NoteLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        var note: DiraNote? = null
            get() = field!!
            set(value) {
                field = value
                bind(value)
            }

        private fun bind(note: DiraNote?) {
            note?.let {
                with(binding) {
                    ViewCompat.setTransitionName(cardView, note.id.toString())
                    title.text = note.title
                    description.text = note.description
                    info.text = note.score.toString() + " очков"

                    cardView.playFadeInAnimation(500L)

                    root.setOnLongClickListener {
                        listener.onNoteSelected(note, it as CardView)
                        true
                    }

                    root.setOnClickListener {
                        listener.onNoteClicked(note, it as CardView)
                    }
                }
            }
        }
    }
}