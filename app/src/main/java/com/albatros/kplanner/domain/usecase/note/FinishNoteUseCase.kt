package com.albatros.kplanner.domain.usecase.note

import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.repo.UserRepository

class FinishNoteUseCase(private val userRepository: UserRepository) {

    operator fun invoke(note: DiraNote) {
        if (note.finished) { return }
        note.finished = true
        with(userRepository.getCurrentUser()) {
            score += note.score
            scoreOfDay += note.score
            scoreOfWeek += note.score
        }
    }
}