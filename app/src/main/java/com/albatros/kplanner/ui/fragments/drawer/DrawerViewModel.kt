package com.albatros.kplanner.ui.fragments.drawer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.albatros.kplanner.domain.usecase.datatransfer.get.GetCurrentUserUseCase
import com.albatros.kplanner.domain.usecase.uimode.GetUiModeUseCase
import com.albatros.kplanner.domain.usecase.uimode.SetUiModeUseCase
import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.model.repo.PreferencesRepository.UiMode
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DrawerViewModel(
    getCurrentUser: GetCurrentUserUseCase,
    private val setUiModeUseCase: SetUiModeUseCase,
    private val getUiModeUseCase: GetUiModeUseCase
) : ViewModel() {

    private val _uiModeFlow = MutableStateFlow(getUiModeUseCase())
    private val uiModeFlow: StateFlow<UiMode> = _uiModeFlow

    fun changeUiMode() {
        val mode = if (getUiModeUseCase() == UiMode.DARK)  UiMode.LIGHT else UiMode.DARK
        setUiModeUseCase(mode)
        viewModelScope.launch {
            _uiModeFlow.emit(mode)
        }
    }

    private val _userFlow = MutableStateFlow(getCurrentUser())
    private val userFlow: StateFlow<DiraUser> = _userFlow

    val uiFlow = combine(uiModeFlow, userFlow) {
        first, second -> Pair(first, second)
    }
}