package com.albatros.kplanner.ui.fragments.enter

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.albatros.kplanner.databinding.EnterFragmentBinding
import com.albatros.kplanner.domain.extensions.playFadeInAnimation
import com.albatros.kplanner.domain.extensions.playFadeOutAnimation
import com.albatros.kplanner.domain.util.AuthResult
import com.albatros.kplanner.model.data.DiraUser
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EnterFragment : Fragment() {

    private lateinit var binding: EnterFragmentBinding
    private val viewModel: EnterViewModel by viewModel()

    private val onAuthCompletedObserver = Observer<AuthResult> {
        with(binding) {
            when (it) {
                is AuthResult.AuthProgress -> {}
                is AuthResult.AuthInvalid -> passwordInput.helperText = "Input data is invalid. Try again."
                is AuthResult.AuthFailure -> binding.passwordInput.helperText = it.exception.message
                is AuthResult.AuthSuccess -> {
                    binding.passwordInput.playFadeOutAnimation(700L)
                    binding.addressInput.playFadeOutAnimation(700L)
                    viewModel.loadInternalUser(it.user)
                }
            }
        }
    }

    private val onInternalUserFetched = Observer<DiraUser?> {
        with(binding) {
            if (it == null) {
                passwordInput.helperText = "Internal server error. Try again."
                passwordInput.playFadeInAnimation(500L)
                addressInput.playFadeInAnimation(500L)
                return@Observer
            }

            lifecycleScope.launch {
                register.playFadeOutAnimation(700L)
                enterText.playFadeOutAnimation(700L)
                enterText.isClickable = false
                delay(700L)
                val direction = EnterFragmentDirections.actionEnterFragmentToWelcomeFragment()
                findNavController().navigate(direction)
            }
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
        setHasOptionsMenu(false)

        postponeEnterTransition()
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.move)
        startPostponedEnterTransition()

        viewModel.lastEnterData.observe(viewLifecycleOwner) {
            binding.addressEdit.setText(it.first)
            binding.passwordEdit.setText(it.second)
        }

        viewModel.authResult.observe(viewLifecycleOwner, onAuthCompletedObserver)
        viewModel.diraUser.observe(viewLifecycleOwner, onInternalUserFetched)

        with(binding) {
            enterText.setOnClickListener { processUserData() }
            register.setOnClickListener {
                val direction = EnterFragmentDirections
                    .actionEnterFragmentToRegisterFragment()
                findNavController().navigate(direction)
            }
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
