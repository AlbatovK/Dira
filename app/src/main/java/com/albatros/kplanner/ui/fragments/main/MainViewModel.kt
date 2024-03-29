package com.albatros.kplanner.ui.fragments.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.domain.usecase.datatransfer.fetch.InternalDataFetchUseCases
import com.albatros.kplanner.domain.usecase.datatransfer.get.GetCurrentUserUseCase
import com.albatros.kplanner.domain.usecase.datatransfer.input.ServerInputUseCases
import com.albatros.kplanner.domain.usecase.note.NotesUseCases
import com.albatros.kplanner.model.alarm.AndroidAlarmScheduler
import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.data.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val notesUseCases: NotesUseCases,
    private val internalDataFetchUseCases: InternalDataFetchUseCases,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val serverInputUseCases: ServerInputUseCases,
    private val alarmScheduler: AndroidAlarmScheduler,
) : ViewModel() {

    private val _schedule: MutableLiveData<Schedule> = MutableLiveData<Schedule>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                value = internalDataFetchUseCases.loadUsersSchedule(getCurrentUser().tokenId)
            } catch (e: Exception) {
            }
        }
    }

    fun scheduleTask(task: DiraNote) {
        alarmScheduler.schedule(task)
    }

    val schedule: LiveData<Schedule> = _schedule

    fun removeNoteFromSchedule(pos: Int) {
        try {
            notesUseCases.removeNoteAtFromSchedule(pos)
        } catch (e: Exception) {
        }
    }

    fun moveNote(from: Int, to: Int) {
        try {
            notesUseCases.changeNotePosition(from, to)
        } catch (e: Exception) {
        }
    }

    fun finishNote(note: DiraNote) {
        try {
            notesUseCases.finishNote(note)
        } catch (e: Exception) {
        }
        saveState()
    }

    fun getUsersDayScore(): Int {
        return getCurrentUser().scoreOfDay
    }

    fun saveState() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                serverInputUseCases.saveSchedule()
                serverInputUseCases.saveUser()
            } catch (e: Exception) {
            }
        }
    }
}