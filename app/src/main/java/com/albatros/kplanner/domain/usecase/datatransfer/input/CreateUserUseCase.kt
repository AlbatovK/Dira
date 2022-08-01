package com.albatros.kplanner.domain.usecase.datatransfer.input

import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.model.repo.UserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateUserUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(user: FirebaseUser): DiraUser = withContext(Dispatchers.IO) {
        val diraUser = DiraUser(user.uid, 0, user.email!!, user.email!!.split("@")[0])
        userRepository.saveUser(diraUser)
        diraUser
    }
}