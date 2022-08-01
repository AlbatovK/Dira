package com.albatros.kplanner.ui.fragments.drawer

import androidx.lifecycle.ViewModel
import com.albatros.kplanner.domain.usecase.datatransfer.get.GetCurrentUserUseCase
import com.albatros.kplanner.model.data.DiraUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DrawerViewModel(getCurrentUser: GetCurrentUserUseCase) : ViewModel() {

    private val _userFlow = MutableStateFlow(getCurrentUser())
    val userFlow: StateFlow<DiraUser> = _userFlow

}