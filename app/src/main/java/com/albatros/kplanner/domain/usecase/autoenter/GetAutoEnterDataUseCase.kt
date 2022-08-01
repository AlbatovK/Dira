package com.albatros.kplanner.domain.usecase.autoenter

import com.albatros.kplanner.model.repo.PreferencesRepository

class GetAutoEnterDataUseCase(private val preferencesRepository: PreferencesRepository) {
    operator fun invoke(): Pair<String, String> =
        preferencesRepository.getEmail()!! to preferencesRepository.getPassword()!!
}