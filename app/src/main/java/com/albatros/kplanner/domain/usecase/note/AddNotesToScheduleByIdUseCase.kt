package com.albatros.kplanner.domain.usecase.note

import com.albatros.kplanner.domain.util.NotesIdsResource
import com.albatros.kplanner.model.repo.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddNotesToScheduleByIdUseCase(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(noteIds: List<Long>) =
        withContext(Dispatchers.IO) { noteRepository.addNoteWithIds(NotesIdsResource(noteIds)) }
}