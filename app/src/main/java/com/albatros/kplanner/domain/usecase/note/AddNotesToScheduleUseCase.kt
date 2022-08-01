package com.albatros.kplanner.domain.usecase.note

import com.albatros.kplanner.domain.util.NotesIdsResource
import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.repo.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddNotesToScheduleUseCase(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(notes: List<DiraNote>) =
        withContext(Dispatchers.IO) {
            val ids = notes.map(DiraNote::id)
            noteRepository.addNoteWithIds(NotesIdsResource(ids))
        }
}