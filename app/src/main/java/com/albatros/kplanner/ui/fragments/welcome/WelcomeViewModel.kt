package com.albatros.kplanner.ui.fragments.welcome

import androidx.lifecycle.ViewModel
import com.albatros.kplanner.model.repo.UserRepo

class WelcomeViewModel(private val repo: UserRepo) : ViewModel() {
    fun getUserNickname() = repo.diraUser.nickname
}