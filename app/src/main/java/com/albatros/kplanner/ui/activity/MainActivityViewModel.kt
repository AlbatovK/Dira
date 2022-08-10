package com.albatros.kplanner.ui.activity

import androidx.lifecycle.ViewModel
import com.albatros.kplanner.domain.usecase.uimode.GetUiModeUseCase

class MainActivityViewModel(private val getUiModeUseCase: GetUiModeUseCase) : ViewModel() {
    fun getUiMode() = getUiModeUseCase()
}