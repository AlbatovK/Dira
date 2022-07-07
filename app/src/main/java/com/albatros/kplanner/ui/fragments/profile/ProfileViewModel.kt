package com.albatros.kplanner.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.model.api.DiraApi
import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.model.data.Schedule
import com.albatros.kplanner.model.repo.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val repo: UserRepo, private val api: DiraApi, private val chosen: DiraUser) : ViewModel() {

    private val _userSchedule: MutableLiveData<Schedule> = MutableLiveData<Schedule>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            value = api.getScheduleByOwnerId(chosen.tokenId)
        }
    }

    val userSchedule: LiveData<Schedule> = _userSchedule

    private val _onImported: MutableLiveData<Boolean> = MutableLiveData(false)
    val onImported: LiveData<Boolean> = _onImported

    fun importSchedule() {
        viewModelScope.launch(Dispatchers.Main) {
            val schedule = api.getScheduleByOwnerId(chosen.tokenId).copy(ownerId = repo.diraUser.tokenId)
            schedule.tasks.forEach { it.finished = false }
            api.createSchedule(schedule)
            repo.schedule = schedule
            _onImported.value = true
        }
    }

}