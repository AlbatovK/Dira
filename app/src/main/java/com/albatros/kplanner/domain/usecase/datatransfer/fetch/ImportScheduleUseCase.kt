package com.albatros.kplanner.domain.usecase.datatransfer.fetch

import com.albatros.kplanner.domain.usecase.datatransfer.get.GetCurrentUserUseCase
import com.albatros.kplanner.domain.usecase.datatransfer.input.SaveScheduleUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ImportScheduleUseCase(
    private val loadUsersSchedule: LoadUsersScheduleUseCase,
    private val getCurrentUser: GetCurrentUserUseCase,
    private val saveScheduleUseCase: SaveScheduleUseCase
) {

    suspend operator fun invoke(ownerUserId: String) = withContext(Dispatchers.IO) {
        loadUsersSchedule(ownerUserId).copy(ownerId = getCurrentUser().tokenId).apply {
            tasks.forEach { it.finished = false }
        }.also { saveScheduleUseCase(schedule = it) }
    }
}