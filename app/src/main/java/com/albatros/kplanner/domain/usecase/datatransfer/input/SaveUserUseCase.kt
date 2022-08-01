package com.albatros.kplanner.domain.usecase.datatransfer.input

import com.albatros.kplanner.model.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() =
        withContext(Dispatchers.IO) { userRepository.saveUser(userRepository.getCurrentUser()) }
}