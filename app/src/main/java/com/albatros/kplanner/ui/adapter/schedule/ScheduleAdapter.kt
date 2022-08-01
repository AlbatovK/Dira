package com.albatros.kplanner.ui.adapter.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.UserNoteLayoutBinding
import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.data.Schedule
import java.util.*

class ScheduleAdapter(
    private val schedule: Schedule,
    private val listener: ScheduleAdapterListener
) : RecyclerView.Adapter<ScheduleAdapter.UserNoteViewHolder>() {

    override fun getItemCount(): Int = schedule.tasks.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserNoteViewHolder {
        val binding =
            UserNoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserNoteViewHolder(binding)
    }

    fun onItemDismiss(position: Int) =
        listener.onItemDismiss(position, this)

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean =
        listener.onItemMoved(fromPosition, toPosition, this)

    override fun onBindViewHolder(holder: UserNoteViewHolder, position: Int) {
        holder.note = schedule.tasks[position]
    }

    inner class UserNoteViewHolder(
        private val binding: UserNoteLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

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
                    info.text = root.context.getString(R.string.points_template_str, note.score)

                    if (note.finished) {
                        doneImg.setImageResource(R.drawable.ic_checkbox_checked)
                        doneImg.setColorFilter(
                            root.context.resources.getColor(
                                R.color.neon_green,
                                root.context.theme
                            )
                        )
                    }

                    doneImg.setOnClickListener {
                        listener.onNoteFinished(note, schedule, binding.doneImg)
                    }
                }
            }
        }
    }
}