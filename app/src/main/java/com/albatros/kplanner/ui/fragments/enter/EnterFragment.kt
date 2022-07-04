package com.albatros.kplanner.ui.fragments.enter

import android.os.Bundle
import android.text.InputType
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.albatros.kplanner.databinding.EnterFragmentBinding
import com.albatros.kplanner.domain.playFadeInAnimation
import com.albatros.kplanner.domain.playFadeOutAnimation
import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.model.util.EnterResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EnterFragment : Fragment() {

    private lateinit var binding: EnterFragmentBinding
    private val viewModel: EnterViewModel by viewModel()

    private val onUserEntered = Observer<EnterResult> {
        when (it) {
            is EnterResult.EntryInvalid ->  binding.passwordInput.helperText = "Input data is invalid. Try again."
            is EnterResult.EntryStarted -> { }
            is EnterResult.EntryFailure -> binding.passwordInput.helperText = it.exception.message
            is EnterResult.EntrySuccess -> {
                binding.passwordInput.playFadeOutAnimation(1000L)
                binding.addressInput.playFadeOutAnimation(1000L)
                viewModel.transformDiraUser(it.user)
            }
        }
    }

    private val onDiraUserCreated = Observer<DiraUser?> {
        if (it == null) {
            binding.passwordInput.helperText = "Internal server error. Try again."
            lifecycleScope.launch {
                binding.passwordInput.playFadeInAnimation(500L)
                binding.addressInput.playFadeInAnimation(500L)
            }
            return@Observer
        }

        binding.enterText.setOnClickListener { }

        lifecycleScope.launch {
            binding.register.playFadeOutAnimation(500L)
            binding.enterText.playFadeOutAnimation(500L)
            delay(700)

            val direction = EnterFragmentDirections.actionEnterFragmentToWelcomeFragment()
            findNavController().navigate(direction)
        }
    }

    private fun processUserData() {
        with(binding) {
            binding.addressInput.helperText = ""
            binding.passwordInput.helperText = ""
            viewModel.authenticate(
                addressEdit.text.toString(),
                passwordEdit.text.toString()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        viewModel.userEntryLive.observe(viewLifecycleOwner, onUserEntered)
        viewModel.diraUser.observe(viewLifecycleOwner, onDiraUserCreated)

        binding.passwordEdit.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

        binding.register.setOnClickListener {
            val direction = EnterFragmentDirections.actionEnterFragmentToRegisterFragment()
            findNavController().navigate(direction)
        }

        binding.enterText.setOnClickListener {
            processUserData()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EnterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
}
