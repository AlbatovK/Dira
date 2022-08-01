package com.albatros.kplanner.ui.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.albatros.kplanner.R
import com.albatros.kplanner.databinding.WelcomeFragmentBinding
import com.albatros.kplanner.domain.extensions.playFadeInAnimation
import com.albatros.kplanner.domain.extensions.playFadeOutAnimation
import com.albatros.kplanner.ui.activity.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class WelcomeFragment : Fragment(), MainActivity.IOnBackPressed {

    private val viewModel: WelcomeViewModel by viewModel()
    private lateinit var binding: WelcomeFragmentBinding

    override fun onBackPressed(): Boolean {
        activity?.finish()
        activity?.finishAffinity()
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WelcomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)

        viewModel.userNickname.observe(viewLifecycleOwner) {
            binding.welcomeText.text = requireContext().getString(R.string.welcome_txt, it)
        }

        lifecycleScope.launch {
            binding.welcomeText.playFadeInAnimation(300L)
            delay(300L)

            val animation = TranslateAnimation(0f, 0f, 0f, -400f)
            animation.duration = 1000L
            animation.fillAfter = true
            binding.welcomeText.startAnimation(animation)
            delay(2000L)
            binding.welcomeText.playFadeOutAnimation(300L)
            delay(300L)

            val direction = WelcomeFragmentDirections.actionWelcomeFragmentToNavigationFragment()
            findNavController().navigate(direction)
        }
    }
}