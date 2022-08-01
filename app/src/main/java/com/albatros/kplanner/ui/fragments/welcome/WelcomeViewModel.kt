package com.albatros.kplanner.ui.fragments.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.albatros.kplanner.domain.usecase.datatransfer.get.GetCurrentUserUseCase

class WelcomeViewModel(getCurrentUser: GetCurrentUserUseCase) : ViewModel() {

    private val _userNickname = MutableLiveData(getCurrentUser().nickname)
    val userNickname: LiveData<String> = _userNickname

}