package com.albatros.kplanner.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.domain.usecase.datatransfer.fetch.ImportScheduleUseCase
import com.albatros.kplanner.domain.usecase.datatransfer.fetch.InternalDataFetchUseCases
import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.model.data.Schedule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val internalDataFetchUseCases: InternalDataFetchUseCases,
    private val importScheduleUseCase: ImportScheduleUseCase,
    private val chosen: DiraUser
) : ViewModel() {

    private val _userSchedule: MutableLiveData<Schedule> = MutableLiveData<Schedule>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            value = internalDataFetchUseCases.loadUsersSchedule(chosen.tokenId)
        }
    }

    val userSchedule: LiveData<Schedule> = _userSchedule

    private val _onImported: MutableLiveData<Boolean> = MutableLiveData(false)

    val onImported: LiveData<Boolean> = _onImported

    fun importSchedule() {
        viewModelScope.launch(Dispatchers.Main) {
            importScheduleUseCase(chosen.tokenId)
            _onImported.value = true
        }
    }

}