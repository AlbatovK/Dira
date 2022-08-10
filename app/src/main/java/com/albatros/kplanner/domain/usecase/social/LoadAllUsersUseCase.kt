package com.albatros.kplanner.domain.usecase.social

import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.model.network.DiraApi
import com.albatros.kplanner.model.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadAllUsersUseCase(private val api: DiraApi, private val userRepository: UserRepository) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        api.getUsers(0, 60).sortedByDescending(DiraUser::score)
            .toMutableList().also { it.remove(userRepository.getCurrentUser()) }
    }
}