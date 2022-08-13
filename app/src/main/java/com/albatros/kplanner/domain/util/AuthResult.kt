package com.albatros.kplanner.domain.util

import com.google.firebase.auth.FirebaseUser

sealed class AuthResult {
    data class AuthSuccess(val user: FirebaseUser) : AuthResult()
    data class AuthFailure(val exception: Exception) : AuthResult()
    object AuthProgress : AuthResult()
    object AuthInvalid : AuthResult()
}