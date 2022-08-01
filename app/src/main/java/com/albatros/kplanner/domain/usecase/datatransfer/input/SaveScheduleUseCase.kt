package com.albatros.kplanner.domain.usecase.datatransfer.input

import com.albatros.kplanner.model.data.Schedule
import com.albatros.kplanner.model.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SaveScheduleUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(schedule: Schedule? = null) =
        withContext(Dispatchers.IO) { userRepository.saveSchedule(schedule ?: userRepository.getCurrentSchedule()) }
}