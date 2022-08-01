package com.albatros.kplanner.domain.usecase.social

import com.albatros.kplanner.domain.usecase.datatransfer.get.GetCurrentUserUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoadFriendsUseCase(
    private val loadAllUsers: LoadAllUsersUseCase,
    private val getCurrentUser: GetCurrentUserUseCase
) {
    suspend operator fun invoke() = withContext(Dispatchers.IO) {
        val user = getCurrentUser()
        loadAllUsers().filter { user.friendsIds.contains(it.tokenId) }
    }
}