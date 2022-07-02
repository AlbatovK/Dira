package com.albatros.kplanner.ui.fragments.main

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.MainFragmentBinding
import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.data.Schedule
import com.albatros.kplanner.ui.adapter.schedule.ScheduleAdapter
import com.albatros.kplanner.ui.adapter.schedule.ScheduleAdapterListener
import org.koin.androidx.viewmodel.ext.android.viewModel


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
        override fun onNoteFinished(note: DiraNote, schedule: Schedule, view: CardView) {
            viewModel.finishNote(note)
            val new = context?.resources?.getColor(R.color.neon_green, context?.theme)
            val old = context?.resources?.getColor(R.color.white, context?.theme)
            ValueAnimator.ofObject(ArgbEvaluator(), old, new).apply {
                duration = 400
                addUpdateListener { anim -> new?.let { view.setCardBackgroundColor(anim.animatedValue as Int) } }
            }.start()
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