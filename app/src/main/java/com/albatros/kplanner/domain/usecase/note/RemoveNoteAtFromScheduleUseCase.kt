package com.albatros.kplanner.domain.usecase.note

import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.repo.UserRepository

class RemoveNoteAtFromScheduleUseCase(private val userRepository: UserRepository) {
    operator fun invoke(pos: Int) = userRepository.getCurrentSchedule().tasks.removeAt(pos)
}