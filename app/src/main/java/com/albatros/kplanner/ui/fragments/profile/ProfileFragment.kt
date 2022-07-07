package com.albatros.kplanner.ui.fragments.profile

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.ProfileFragmentBinding
import com.albatros.kplanner.domain.getMonth
import com.albatros.kplanner.domain.playFadeInAnimation
import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.data.Schedule
import com.albatros.kplanner.ui.activity.MainActivity
import com.albatros.kplanner.ui.adapter.schedule.ScheduleAdapter
import com.albatros.kplanner.ui.adapter.schedule.ScheduleAdapterListener
import koleton.api.hideSkeleton
import koleton.api.loadSkeleton
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModel { parametersOf(arguments.user) }
    private lateinit var binding: ProfileFragmentBinding
    private val arguments: ProfileFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId) {
        R.id.action_import -> {
            viewModel.importSchedule()
            true
        } else -> false
    }

    private val listener = object: ScheduleAdapterListener {

        override fun OnFirstNoteBinded(view: CardView) {}

        override fun onNoteFinished(note: DiraNote, schedule: Schedule, view: ImageView) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        postponeEnterTransition()
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        viewModel.onImported.observe(viewLifecycleOwner) {
            if (it) {
                (activity as MainActivity).binding.toolbar.menu.findItem(R.id.action_import).setIcon(R.drawable.ic_check)
            }
        }

        binding.date.loadSkeleton {
            color(R.color.gray_light)
            shimmer(true)
        }

        viewModel.userSchedule.observe(viewLifecycleOwner) {
            binding.listProfile.adapter = ScheduleAdapter(it, listener)
            binding.listProfile.layoutManager = LinearLayoutManager(context)
            val calendar = Calendar.getInstance()
            binding.date.text = context?.getString(
                R.string.date_str,
                calendar.get(Calendar.DAY_OF_MONTH),
                getMonth(calendar.get(Calendar.MONTH))
            )
            binding.date.hideSkeleton()
        }

        (activity as MainActivity).binding.toolbar.title = arguments.user.nickname
    }
}