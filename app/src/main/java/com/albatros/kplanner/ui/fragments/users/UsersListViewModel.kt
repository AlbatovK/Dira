package com.albatros.kplanner.ui.fragments.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.domain.usecase.datatransfer.get.GetCurrentUserUseCase
import com.albatros.kplanner.domain.usecase.social.AddFriendsUseCase
import com.albatros.kplanner.domain.usecase.social.LoadAllUsersUseCase
import com.albatros.kplanner.domain.usecase.social.LoadFriendsUseCase
import com.albatros.kplanner.domain.usecase.social.LoadUsersByNamePrefixUseCase
import com.albatros.kplanner.model.data.DiraUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UsersListViewModel(
    private val addFriendUseCase: AddFriendsUseCase,
    private val loadAllUsers: LoadAllUsersUseCase,
    private val loadFriendsUseCase: LoadFriendsUseCase,
    private val loadUsersByNamePrefix: LoadUsersByNamePrefixUseCase,
    private val getCurrentUser: GetCurrentUserUseCase
) : ViewModel() {

    private var allUsers = listOf<DiraUser>()

    private val _users: MutableLiveData<List<DiraUser>> =
        MutableLiveData<List<DiraUser>>().apply {
            viewModelScope.launch(Dispatchers.Main) {
                try {
                    allUsers = loadAllUsers()
                    value = allUsers.toMutableList()
                } catch (e: Exception) {
                }
            }
        }

    private var searchJob: Job? = null

    fun fetchByTopics(query: String) {
        try {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(500L)
                val found = loadUsersByNamePrefix(query)
                _users.value = found
            }
        } catch (e: Exception) {
        }
    }

    fun loadFriends() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val friends = loadFriendsUseCase()
                _users.value = friends
            } catch (e: Exception) {
            }
        }
    }

    fun loadUsersList() {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                _users.value = allUsers
            } catch (e: Exception) {
            }
        }
    }

    val users: LiveData<List<DiraUser>> = _users

    fun getUser() = getCurrentUser()

    fun addFriend(user: DiraUser) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                addFriendUseCase(user)
            } catch (e: Exception) {
            }
        }
    }
}