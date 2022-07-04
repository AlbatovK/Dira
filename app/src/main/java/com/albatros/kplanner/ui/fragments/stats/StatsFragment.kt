package com.albatros.kplanner.ui.fragments.stats

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatDrawableManager
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.StatsFragmentBinding
import com.albatros.kplanner.domain.getLeague
import com.albatros.kplanner.domain.playFadeInAnimation
import com.albatros.kplanner.ui.adapter.user.UserAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class StatsFragment : Fragment() {

    private lateinit var binding: StatsFragmentBinding
    private val viewModel: StatsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = StatsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        viewModel.user.observe(viewLifecycleOwner) { user ->
            val drawable = when(user.league) {
                1 -> AppCompatResources.getDrawable(requireContext(), R.drawable.bronze)
                2 -> AppCompatResources.getDrawable(requireContext(), R.drawable.silver)
                3 -> AppCompatResources.getDrawable(requireContext(), R.drawable.gold)
                4 -> AppCompatResources.getDrawable(requireContext(), R.drawable.platinum)
                5 -> AppCompatResources.getDrawable(requireContext(), R.drawable.diamond)
                else -> AppCompatResources.getDrawable(requireContext(), R.drawable.bronze)
            }
            binding.leagueImg.setImageDrawable(drawable)
            binding.leagueName.text = getLeague(user.league)
            binding.points.text = context?.getString(R.string.points_template_str, user.scoreOfWeek)
            binding.name.text = user.nickname.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        }

        viewModel.leagueUsers.observe(viewLifecycleOwner) {
            binding.list.adapter = UserAdapter(it.toMutableList())
            binding.list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }
}