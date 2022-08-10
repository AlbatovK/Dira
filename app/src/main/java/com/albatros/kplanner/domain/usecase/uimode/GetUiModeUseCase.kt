package com.albatros.kplanner.domain.usecase.uimode

import com.albatros.kplanner.model.repo.PreferencesRepository

class GetUiModeUseCase(private val sharedPreferencesRepository: PreferencesRepository) {
    operator fun invoke() = sharedPreferencesRepository.getUiMode()
}