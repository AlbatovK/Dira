package com.albatros.kplanner.ui.fragments.users

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.model.api.DiraApi
import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.model.data.NotesList
import com.albatros.kplanner.model.repo.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UsersListViewModel(private val api: DiraApi, private val repo: UserRepo) : ViewModel() {

    private var allUsers = mutableListOf<DiraUser>()

    private val _users: MutableLiveData<List<DiraUser>> = MutableLiveData<List<DiraUser>>().apply {
        viewModelScope.launch(Dispatchers.Main) {
            val list = api.getAllUsers().toMutableList()
            list.remove(repo.diraUser)
            allUsers = list.sortedByDescending(DiraUser::score).toMutableList()
            value = list.sortedByDescending(DiraUser::score)
        }
    }

    fun fetchByTopics(query: String) {
        viewModelScope.launch {
            _users.value = try {
                _users.value?.filter {
                    it.nickname.lowercase().contains(query, ignoreCase = true)
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    fun loadFriends() {
        _users.value = _users.value?.filter { repo.diraUser.friendsIds.contains(it.tokenId) }
    }

    fun loadUsersList() {
        viewModelScope.launch(Dispatchers.Main) {
            _users.value = allUsers
        }
    }

    val users: LiveData<List<DiraUser>> = _users

    fun getUser() = repo.diraUser

    fun addFriend(user: DiraUser) {
        viewModelScope.launch(Dispatchers.Main) {
            repo.diraUser.friendsIds.add(user.tokenId)
            api.createUser(repo.diraUser)
        }
    }
}