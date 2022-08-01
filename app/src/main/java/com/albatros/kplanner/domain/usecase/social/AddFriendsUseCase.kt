package com.albatros.kplanner.domain.usecase.social

import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.model.repo.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddFriendsUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(friendUser: DiraUser) = withContext(Dispatchers.IO) {
        val user = userRepository.getCurrentUser()
        user.friendsIds.add(friendUser.tokenId)
        userRepository.saveUser(user)
    }
}