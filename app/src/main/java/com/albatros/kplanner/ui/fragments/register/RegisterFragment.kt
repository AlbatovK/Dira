package com.albatros.kplanner.ui.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.albatros.kplanner.databinding.RegisterFragmentBinding
import com.albatros.kplanner.domain.extensions.playFadeInAnimation
import com.albatros.kplanner.domain.extensions.playFadeOutAnimation
import com.albatros.kplanner.domain.util.AuthResult
import com.albatros.kplanner.ui.activity.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterFragment : Fragment(), MainActivity.IOnBackPressed {

    override fun onBackPressed(): Boolean {
        activity?.finish()
        activity?.finishAffinity()
        return true
    }

    private lateinit var binding: RegisterFragmentBinding
    private val viewModel: RegisterViewModel by viewModel()

    private fun processUserData() {
        with(binding) {
            addressInput.helperText = ""
            passwordInput.helperText = ""
            viewModel.startAuth(
                addressEdit.text.toString(),
                passwordEdit.text.toString()
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(false)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventChannel.collect {
                    when (it) {
                        is RegisterViewModel.UiEvent.OnUserFetchCompleted -> {
                            if (it.user == null) {
                                binding.passwordInput.helperText =
                                    "Internal server error. Try again."
                                binding.passwordInput.playFadeInAnimation(500L)
                                binding.addressInput.playFadeInAnimation(500L)
                                return@collect
                            }

                            lifecycleScope.launch {
                                binding.registerText.isClickable = false
                                binding.registerText.playFadeOutAnimation(700L)
                                delay(700)
                                val direction =
                                    RegisterFragmentDirections.actionRegisterFragmentToWelcomeFragment()
                                findNavController().navigate(direction)
                            }
                        }
                        is RegisterViewModel.UiEvent.OnAuthStateChanged -> {
                            when (it.authResult) {
                                is AuthResult.AuthProgress -> Unit
                                is AuthResult.AuthInvalid -> {
                                    binding.passwordInput.helperText =
                                        "Input data is invalid. Try again."
                                }
                                is AuthResult.AuthFailure -> {
                                    binding.passwordInput.helperText =
                                        it.authResult.exception.message
                                }
                                is AuthResult.AuthSuccess -> {
                                    binding.passwordInput.playFadeOutAnimation(700L)
                                    binding.addressInput.playFadeOutAnimation(700L)
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.registerText.setOnClickListener {
            processUserData()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}