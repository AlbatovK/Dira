package com.albatros.kplanner.domain.usecase.datatransfer.fetch

import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.model.network.DiraApi
import com.albatros.kplanner.model.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadAllByUsersLeagueUseCase(
    private val userRepository: UserRepository,
    private val api: DiraApi
) {
    suspend operator fun invoke() =
        withContext(Dispatchers.IO) {
            api.getUsersByLeague(userRepository.getCurrentUser().league)
                .sortedByDescending(DiraUser::scoreOfWeek)
        }
}