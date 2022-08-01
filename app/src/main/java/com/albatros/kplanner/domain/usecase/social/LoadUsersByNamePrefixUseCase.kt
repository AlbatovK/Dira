package com.albatros.kplanner.domain.usecase.social

import com.albatros.kplanner.model.network.DiraApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadUsersByNamePrefixUseCase(private val api: DiraApi) {
    suspend operator fun invoke(prefix: String) = withContext(Dispatchers.IO) {
        api.getUsersByNamePrefix(prefix)
    }
}