package com.albatros.kplanner.domain.usecase.auth

import com.albatros.kplanner.domain.extensions.isEntryValid
import com.albatros.kplanner.domain.util.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpFirebaseUseCase {

    suspend operator fun invoke(email: String, password: String): AuthResult {

        if (!isEntryValid(email, password))
            return AuthResult.AuthInvalid

        return try {
            withContext(Dispatchers.IO) {
                val gotUser =
                    Firebase.auth.createUserWithEmailAndPassword(email, password).await().user
                AuthResult.AuthSuccess(gotUser!!)
            }
        } catch (e: Exception) {
            AuthResult.AuthFailure(e)
        }
    }
}