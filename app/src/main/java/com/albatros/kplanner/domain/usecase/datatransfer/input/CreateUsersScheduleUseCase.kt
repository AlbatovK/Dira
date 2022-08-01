package com.albatros.kplanner.domain.usecase.datatransfer.input

import com.albatros.kplanner.model.data.Schedule
import com.albatros.kplanner.model.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateUsersScheduleUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(): Schedule = withContext(Dispatchers.IO) {
        val schedule = Schedule(ownerId = userRepository.getCurrentUser().tokenId)
        userRepository.saveSchedule(schedule)
        schedule
    }
}