package com.albatros.kplanner.ui.fragments.main

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.MainFragmentBinding
import com.albatros.kplanner.domain.getMonth
import com.albatros.kplanner.domain.playFadeInAnimation
import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.data.Schedule
import com.albatros.kplanner.ui.adapter.schedule.ScheduleAdapter
import com.albatros.kplanner.ui.adapter.schedule.ScheduleAdapterListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MainFragment : Fragment() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: MainFragmentBinding

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.action_create -> {
            val direction = MainFragmentDirections.actionMainFragmentToListFragment()
            findNavController().navigate(direction)
            true
        }
        else -> false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private val listener = object: ScheduleAdapterListener {
        override fun onNoteFinished(note: DiraNote, schedule: Schedule, view: ImageView) {
            if (note.finished) {
                return
            }
            viewModel.finishNote(note)
            binding.progressBar.progress += 1
            binding.statText.text = context?.getString(R.string.stat_str,
                binding.progressBar.progress,
                binding.progressBar.max
            )
            binding.points.text = context?.getString(R.string.points_str, viewModel.getUsersDayScore())
            val new = context?.resources?.getColor(R.color.neon_green, context?.theme)
            val old = context?.resources?.getColor(R.color.dark_cyan, context?.theme)
            ValueAnimator.ofObject(ArgbEvaluator(), old, new).apply {
                duration = 400
                addUpdateListener { anim -> new?.let { view.setColorFilter(anim.animatedValue as Int) } }
            }.start()
           // Toast.makeText(context, "${Calendar.getInstance().get(Calendar.DAY_OF_MONTH)}", Toast.LENGTH_SHORT).show()
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

        override fun isItemViewSwipeEnabled(): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
            adapter.onItemDismiss(viewHolder.adapterPosition)
            viewModel.saveState()
            binding.progressBar.progress -= 1
            binding.progressBar.max -= 1
            binding.statText.text = context?.getString(R.string.stat_str,
                binding.progressBar.progress,
                binding.progressBar.max
            )
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
        setHasOptionsMenu(true)

        postponeEnterTransition()
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        viewModel.schedule.observe(viewLifecycleOwner) {
            binding.list.adapter = ScheduleAdapter(it, listener)
            val callback: ItemTouchHelper.Callback = ItemTouchHelperCallback(binding.list.adapter as ScheduleAdapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(binding.list)
            binding.list.layoutManager = LinearLayoutManager(context)
            binding.points.text = context?.getString(R.string.points_str, viewModel.getUsersDayScore())
            binding.statText.text = context?.getString(R.string.stat_str,
                it.tasks.count { s -> s.finished },
                it.tasks.size
            )
            binding.progressBar.max = it.tasks.size
            binding.progressBar.progress = it.tasks.count { s -> s.finished }
            binding.cardView.playFadeInAnimation(500L)

            val calendar = Calendar.getInstance()
            binding.date.text = context?.getString(
                R.string.date_str,
                calendar.get(Calendar.DAY_OF_MONTH),
                getMonth(calendar.get(Calendar.MONTH))
            )
            binding.date.playFadeInAnimation(500L)
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