package com.albatros.kplanner.ui.fragments.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.model.api.DiraApi
import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.model.repo.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StatsViewModel(private val api: DiraApi, private val repo: UserRepo) : ViewModel() {

    private val _leagueUsers: MutableLiveData<List<DiraUser>> = MutableLiveData<List<DiraUser>>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            value = api.getUsersByLeague(repo.diraUser.league).sortedByDescending {
                it.scoreOfWeek
            }
        }
    }

    val leagueUsers: LiveData<List<DiraUser>> = _leagueUsers

    private val _user: MutableLiveData<DiraUser> = MutableLiveData(repo.diraUser)
    val user: LiveData<DiraUser> = _user

}