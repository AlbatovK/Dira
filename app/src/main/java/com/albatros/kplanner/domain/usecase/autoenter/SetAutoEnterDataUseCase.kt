package com.albatros.kplanner.domain.usecase.autoenter

import com.albatros.kplanner.model.repo.PreferencesRepository

class SetAutoEnterDataUseCase(private val preferencesRepository: PreferencesRepository) {

    operator fun invoke(email: String, password: String) {
        preferencesRepository.setEmail(email)
        preferencesRepository.setPassword(password)
    }
}