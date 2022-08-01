package com.albatros.kplanner.domain.usecase.note

import com.albatros.kplanner.model.repo.UserRepository
import java.util.*

class ChangeNotePositionUseCase(private val userRepository: UserRepository) {
    operator fun invoke(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(userRepository.getCurrentSchedule().tasks, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(userRepository.getCurrentSchedule().tasks, i, i - 1)
            }
        }
    }
}