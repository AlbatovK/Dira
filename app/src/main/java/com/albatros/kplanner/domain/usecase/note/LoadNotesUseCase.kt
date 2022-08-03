package com.albatros.kplanner.domain.usecase.note

import com.albatros.kplanner.model.repo.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadNotesUseCase(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(from: Int, to: Int) = withContext(Dispatchers.IO) {
        noteRepository.getNotes(from, to)
    }
}