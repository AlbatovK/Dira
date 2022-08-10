package com.albatros.kplanner.domain.usecase.uimode

import com.albatros.kplanner.model.repo.PreferencesRepository

class SetUiModeUseCase(private val sharedPreferencesRepository: PreferencesRepository) {
    operator fun invoke(mode: PreferencesRepository.UiMode) = sharedPreferencesRepository.setMode(mode)
}