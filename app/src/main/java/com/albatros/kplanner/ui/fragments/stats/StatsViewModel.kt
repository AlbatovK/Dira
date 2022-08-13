package com.albatros.kplanner.ui.fragments.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.domain.usecase.datatransfer.fetch.LoadAllByUsersLeagueUseCase
import com.albatros.kplanner.domain.usecase.datatransfer.get.GetCurrentUserUseCase
import com.albatros.kplanner.model.data.DiraUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatsViewModel(
    loadAllByUsersLeague: LoadAllByUsersLeagueUseCase,
    getCurrentUser: GetCurrentUserUseCase
) : ViewModel() {

    private val _leagueUsers: MutableLiveData<List<DiraUser>> =
        MutableLiveData<List<DiraUser>>().apply {
            viewModelScope.launch(Dispatchers.Main) {
                try {
                    value = loadAllByUsersLeague()
                } catch (e: Exception) {
                }
            }
        }

    val leagueUsers: LiveData<List<DiraUser>> = _leagueUsers

    private val _user: MutableLiveData<DiraUser> = MutableLiveData(getCurrentUser())
    val user: LiveData<DiraUser> = _user

}