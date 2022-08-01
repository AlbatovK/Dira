package com.albatros.kplanner.domain.usecase.auth

data class AuthUseCases(
    val signInFirebase: SignInFirebaseUseCase,
    val signUpFirebase: SignUpFirebaseUseCase
)