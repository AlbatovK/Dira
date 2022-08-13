package com.albatros.kplanner.model.repo

import kotlinx.coroutines.flow.Flow

interface ConnectivityRepository {

    fun getStatus(): Flow<ConnectionStatus>

    enum class ConnectionStatus {
        Available, Unavailable, Losing, Lost
    }
}