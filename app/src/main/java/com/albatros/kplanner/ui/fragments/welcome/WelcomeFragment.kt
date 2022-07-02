package com.albatros.kplanner.ui.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.albatros.kplanner.databinding.WelcomeFragmentBinding
import com.albatros.kplanner.domain.playFadeInAnimation
import com.albatros.kplanner.domain.playFadeOutAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class WelcomeFragment : Fragment() {

    private val viewModel: WelcomeViewModel by viewModel()
    private lateinit var binding: WelcomeFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WelcomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = "Добро пожаловать" + "\n" + viewModel.getUserNickname()
        binding.welcomeText.text = text

        lifecycleScope.launch {
            binding.welcomeText.playFadeInAnimation(500L)
            delay(500)

            val animation = TranslateAnimation(0f, 0f, 0f, -400f)
            animation.duration = 1000
            animation.fillAfter = true
            binding.welcomeText.startAnimation(animation)
            delay(2000)
            binding.welcomeText.playFadeOutAnimation(500L)
            delay(500L)

            val direction = WelcomeFragmentDirections.actionWelcomeFragmentToMainFragment()
            findNavController().navigate(direction)
        }
    }
}