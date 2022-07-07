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
import androidx.lifecycle.lifecycleScope
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
import com.albatros.kplanner.ui.activity.MainActivity
import com.albatros.kplanner.ui.adapter.note.NoteAdapter
import com.albatros.kplanner.ui.adapter.note.NoteAdapterListener
import com.albatros.kplanner.ui.adapter.schedule.ScheduleAdapter
import com.albatros.kplanner.ui.adapter.schedule.ScheduleAdapterListener
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.getkeepsafe.taptargetview.TapTargetView
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MainFragment : Fragment(), MainActivity.IOnBackPressed {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var binding: MainFragmentBinding

    override fun onBackPressed(): Boolean {
        activity?.finish()
        activity?.finishAffinity()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.action_create -> {
            val direction = MainFragmentDirections.actionMainFragmentToListFragment()
            findNavController().navigate(direction)
            true
        }
        R.id.action_people -> {
            val direction = MainFragmentDirections.actionMainFragmentToUsersListFragment()
            findNavController().navigate(direction)
            true
        }
        R.id.action_stats -> {
            val direction = MainFragmentDirections.actionMainFragmentToStatsFragment()
            findNavController().navigate(direction)
            true
        }
        else -> false
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        (activity as MainActivity).binding.toolbar.inflateMenu(R.menu.menu_main)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private var hintView: CardView? = null

    private val listener = object: ScheduleAdapterListener {
        override fun OnFirstNoteBinded(view: CardView) {
            hintView = view
        }

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
            view.setImageResource(R.drawable.ic_checkbox_checked)
            ValueAnimator.ofObject(ArgbEvaluator(), old, new).apply {
                duration = 400
                addUpdateListener { anim -> new?.let { view.setColorFilter(anim.animatedValue as Int) } }
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
        (activity as MainActivity).binding.toolbar.inflateMenu(R.menu.menu_main)

        binding.constraint.loadSkeleton {
            color(R.color.gray_light)
            shimmer(true)
        }

        binding.date.loadSkeleton {
            color(R.color.gray_light)
            shimmer(true)
        }

        lifecycleScope.launchWhenCreated {
            delay(1000)
           if (viewModel.showHints()) {
                viewModel.setOpened()
            val toolbar = (activity as MainActivity).binding.toolbar
            TapTargetSequence(activity as MainActivity).targets(
                TapTarget.forToolbarMenuItem(toolbar, R.id.action_create, getString(R.string.add_hint), getString(R.string.add_descr)).cancelable(true),
                TapTarget.forToolbarMenuItem(toolbar, R.id.action_people, getString(R.string.people_hint), getString(R.string.people_descr)).cancelable(true),
                TapTarget.forToolbarMenuItem(toolbar, R.id.action_stats, getString(R.string.stats_hint), getString(R.string.stats_descr)).cancelable(true)
                ).listener(
                object : TapTargetSequence.Listener {
                    override fun onSequenceFinish() {
                        if (hintView == null)
                            return
                        TapTargetView.showFor(activity as MainActivity,
                            TapTarget.forView(hintView, getString(R.string.item_hint),
                                getString(R.string.item_descr))
                                .cancelable(true)
                                .descriptionTextSize(16)
                                .titleTextSize(18)
                                .tintTarget(true)
                            .drawShadow(true)
                                .targetRadius(80)
                            .tintTarget(true)
                            .transparentTarget(true)
                            .outerCircleColor(R.color.dark_cyan),
                            object : TapTargetView.Listener() {
                                override fun onTargetClick(view: TapTargetView) {
                                    super.onTargetClick(view)
                                    view.dismiss(true)
                                }
                            })
                    }

                    override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {
                    }

                    override fun onSequenceCanceled(lastTarget: TapTarget?) {
                    }
                }
                ).start()
           }
        }

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