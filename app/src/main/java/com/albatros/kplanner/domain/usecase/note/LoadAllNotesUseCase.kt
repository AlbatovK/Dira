package com.albatros.kplanner.domain.usecase.note

import com.albatros.kplanner.model.repo.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadAllNotesUseCase(private val noteRepository: NoteRepository) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        noteRepository.getAllNotes()
    }
}