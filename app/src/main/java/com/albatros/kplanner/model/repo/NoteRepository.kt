package com.albatros.kplanner.model.repo

import com.albatros.kplanner.domain.util.NotesIdsResource
import com.albatros.kplanner.model.network.DiraApi

class NoteRepository(private val api: DiraApi, private val userRepository: UserRepository) {

    suspend fun addNoteWithIds(idsList: NotesIdsResource) {
        api.addNotes(idsList, userRepository.getCurrentUser().tokenId)
    }

    suspend fun getAllNotes() = api.getAllNotes()
}