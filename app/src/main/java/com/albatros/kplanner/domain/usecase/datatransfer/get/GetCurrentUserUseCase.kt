package com.albatros.kplanner.domain.usecase.datatransfer.get

import com.albatros.kplanner.model.repo.UserRepository

class GetCurrentUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke() = userRepository.getCurrentUser()
}