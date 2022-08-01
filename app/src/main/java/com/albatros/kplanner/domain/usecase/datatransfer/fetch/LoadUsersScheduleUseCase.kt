package com.albatros.kplanner.domain.usecase.datatransfer.fetch

import com.albatros.kplanner.model.data.Schedule
import com.albatros.kplanner.model.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadUsersScheduleUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(userId: String): Schedule =
        withContext(Dispatchers.IO) { userRepository.loadSchedule(userId) }
}