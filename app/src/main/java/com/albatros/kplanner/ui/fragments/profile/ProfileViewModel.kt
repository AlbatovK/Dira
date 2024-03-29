package com.albatros.kplanner.ui.fragments.profile

import androidx.lifecycle.*
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

    val userSchedule: LiveData<Schedule> = liveData {
        try {
            emit(internalDataFetchUseCases.loadUsersSchedule(chosen.tokenId))
        } catch (e: Exception) {
        }
    }

    private val _onImported: MutableLiveData<Boolean> = MutableLiveData(false)

    val onImported: LiveData<Boolean> = _onImported

    fun importSchedule() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                importScheduleUseCase(chosen.tokenId)
                _onImported.value = true
            } catch (e: Exception) {
            }
        }
    }

}