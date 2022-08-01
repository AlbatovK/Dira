package com.albatros.kplanner.domain.usecase.datatransfer.fetch

import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.model.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadInternalUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(firebaseUserId: String): DiraUser =
        withContext(Dispatchers.IO) { userRepository.loadUser(firebaseUserId) }
}