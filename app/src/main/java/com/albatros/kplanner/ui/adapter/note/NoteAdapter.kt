package com.albatros.kplanner.ui.adapter.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.NoteLayoutBinding
import com.albatros.kplanner.domain.extensions.playFadeInAnimation
import com.albatros.kplanner.model.data.DiraNote

class NoteAdapter(
    private val notes: MutableList<DiraNote>,
    private val listener: NoteAdapterListener,
    private val isSkeleton: Boolean,
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
            if (isSkeleton)
                return
            note?.let {
                with(binding) {
                    title.text = note.title
                    description.text = note.description
                    info.text = root.context.getString(R.string.points_template_str, note.score)

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