package com.albatros.kplanner.ui.adapter.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.UserNoteLayoutBinding
import com.albatros.kplanner.domain.playFadeInAnimation
import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.data.Schedule
import java.util.*

class ScheduleAdapter(
    private val schedule: Schedule,
    private val listener: ScheduleAdapterListener
) : RecyclerView.Adapter<ScheduleAdapter.UserNoteViewHolder>() {

    override fun getItemCount(): Int = schedule.tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserNoteViewHolder {
        val binding = UserNoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserNoteViewHolder(binding)
    }

    fun onItemDismiss(position: Int) {
        schedule.tasks.removeAt(position)
        notifyItemRemoved(position)
    }

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(schedule.tasks, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(schedule.tasks, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    override fun onBindViewHolder(holder: UserNoteViewHolder, position: Int) { holder.note = schedule.tasks[position] }

    inner class UserNoteViewHolder(private val binding: UserNoteLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        var note: DiraNote? = null
            get() = field!!
            set(value) {
                field = value
                bind(value)
            }

        private fun bind(note: DiraNote?) {
            note?.let {
                with(binding) {
                    title.text = note.title
                    description.text = note.description
                    info.text = note.score.toString() + " очков"

                    cardView.playFadeInAnimation(500L)
                    if (note.finished) {
                        cardView.setCardBackgroundColor(root.context.resources.getColor(R.color.neon_green, root.context.theme))
                    }

                    doneImg.setOnClickListener {
                        listener.onNoteFinished(note, schedule, cardView)
                    }
                }
            }
        }
    }
}