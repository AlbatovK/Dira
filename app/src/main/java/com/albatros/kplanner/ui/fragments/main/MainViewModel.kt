package com.albatros.kplanner.ui.fragments.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.domain.usecase.datatransfer.fetch.InternalDataFetchUseCases
import com.albatros.kplanner.domain.usecase.datatransfer.get.GetCurrentUserUseCase
import com.albatros.kplanner.domain.usecase.datatransfer.input.ServerInputUseCases
import com.albatros.kplanner.domain.usecase.note.NotesUseCases
import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.data.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val notesUseCases: NotesUseCases,
    private val internalDataFetchUseCases: InternalDataFetchUseCases,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val serverInputUseCases: ServerInputUseCases
) : ViewModel() {

    private val _schedule: MutableLiveData<Schedule> = MutableLiveData<Schedule>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            value = internalDataFetchUseCases.loadUsersSchedule(getCurrentUser().tokenId)
        }
    }

    val schedule: LiveData<Schedule> = _schedule

    fun removeNoteFromSchedule(pos: Int) {
        notesUseCases.removeNoteAtFromSchedule(pos)
    }

    fun moveNote(from: Int, to: Int) {
        notesUseCases.changeNotePosition(from, to)
    }

    fun finishNote(note: DiraNote) {
        notesUseCases.finishNote(note)
        saveState()
    }

    fun getUsersDayScore(): Int {
        return getCurrentUser().scoreOfDay
    }

    fun saveState() {
        viewModelScope.launch(Dispatchers.Main) {
            serverInputUseCases.saveSchedule()
            serverInputUseCases.saveUser()
        }
    }
}