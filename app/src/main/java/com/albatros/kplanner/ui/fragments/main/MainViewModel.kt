package com.albatros.kplanner.ui.fragments.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.model.api.DiraApi
import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.data.Schedule
import com.albatros.kplanner.model.repo.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val api: DiraApi, private val repo: UserRepo) : ViewModel() {

    private val _schedule: MutableLiveData<Schedule> = MutableLiveData<Schedule>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            repo.schedule = api.getScheduleByOwnerId(repo.diraUser.tokenId)
            value = repo.schedule
        }
    }

    val schedule: LiveData<Schedule> = _schedule

    fun finishNote(note: DiraNote) {
        note.finished = true
        repo.diraUser.score += note.score
        repo.diraUser.scoreOfDay += note.score
        repo.diraUser.scoreOfWeek += note.score
        saveState()
    }

    fun getUsersDayScore(): Int {
        return repo.diraUser.scoreOfDay
    }

    fun saveState() {
        viewModelScope.launch(Dispatchers.Main) {
            api.createSchedule(repo.schedule)
            api.createUser(repo.diraUser)
        }
    }
}