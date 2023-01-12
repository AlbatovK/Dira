package com.albatros.kplanner.ui.fragments.main

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.MainFragmentBinding
import com.albatros.kplanner.domain.extensions.getMonth
import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.data.Schedule
import com.albatros.kplanner.ui.adapter.schedule.ScheduleAdapter
import com.albatros.kplanner.ui.adapter.schedule.ScheduleAdapterListener
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: MainFragmentBinding

    private val listener = object : ScheduleAdapterListener {

        override fun onItemDismiss(position: Int, adapter: ScheduleAdapter) {
            viewModel.removeNoteFromSchedule(position)
            adapter.notifyItemRemoved(position)
        }

        override fun onTimePicked(note: DiraNote, schedule: Schedule, view: TextView) {
            val zone = TimeZone.getDefault()
            val calendar: Calendar = Calendar.getInstance(zone)
            if (note.time > 0) {
                calendar.timeInMillis = note.time
            }

            val onTimeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                val new = Calendar.getInstance(zone)
                new.set(Calendar.HOUR_OF_DAY, hourOfDay)
                new.set(Calendar.MINUTE, minute)
                note.time = new.timeInMillis
                view.text = DateFormat.format("kk:mm", new)
                viewModel.saveState()
                viewModel.scheduleTask(note)
            }

            TimePickerDialog(requireContext(), onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false).show()
        }

        override fun onItemMoved(
            fromPosition: Int,
            toPosition: Int,
            adapter: ScheduleAdapter
        ): Boolean {
            viewModel.moveNote(fromPosition, toPosition)
            adapter.notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onNoteFinished(note: DiraNote, schedule: Schedule, view: ImageView) {
            viewModel.finishNote(note)

            with(binding) {
                progressBar.progress += 1
                statText.text =
                    context?.getString(R.string.stat_str, progressBar.progress, progressBar.max)
                points.text = context?.getString(R.string.points_str, viewModel.getUsersDayScore())
                val new = context?.resources?.getColor(R.color.neon_green, context?.theme)
                val old = view.imageTintList?.defaultColor

                view.setImageResource(R.drawable.ic_checkbox_checked)
                ValueAnimator.ofObject(ArgbEvaluator(), old, new).apply {
                    duration = 400
                    addUpdateListener { anim -> new?.let { view.setColorFilter(anim.animatedValue as Int) } }
                }.start()
            }
        }
    }

    inner class ItemTouchHelperCallback(private val adapter: ScheduleAdapter) :
        ItemTouchHelper.Callback() {

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun isItemViewSwipeEnabled(): Boolean = true

        override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
            adapter.onItemDismiss(viewHolder.adapterPosition)
            viewModel.saveState()
            with(binding) {
                progressBar.progress -= 1
                progressBar.max -= 1
                statText.text = context?.getString(
                    R.string.stat_str,
                    progressBar.progress,
                    progressBar.max
                )
            }
        }

        override fun onMove(
            recyclerView: RecyclerView, viewHolder: ViewHolder,
            target: ViewHolder
        ): Boolean {
            adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
            viewModel.saveState()
            return true
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        binding.constraint.loadSkeleton {
            color(R.color.gray_light)
            shimmer(true)
        }

        binding.date.loadSkeleton {
            color(R.color.gray_light)
            shimmer(true)
        }

        viewModel.schedule.observe(viewLifecycleOwner) {
            binding.list.adapter = ScheduleAdapter(it, listener)
            val callback: ItemTouchHelper.Callback =
                ItemTouchHelperCallback(binding.list.adapter as ScheduleAdapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(binding.list)
            binding.list.layoutManager = LinearLayoutManager(context)

            binding.points.text =
                context?.getString(R.string.points_str, viewModel.getUsersDayScore())
            binding.statText.text = context?.getString(
                R.string.stat_str,
                it.tasks.count { s -> s.finished },
                it.tasks.size
            )
            binding.progressBar.max = it.tasks.size
            binding.progressBar.progress = it.tasks.count { s -> s.finished }

            val calendar = Calendar.getInstance()
            binding.date.text = context?.getString(
                R.string.date_str,
                calendar.get(Calendar.DAY_OF_MONTH),
                getMonth(calendar.get(Calendar.MONTH))
            )

            binding.date.hideSkeleton()
            binding.constraint.hideSkeleton()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}