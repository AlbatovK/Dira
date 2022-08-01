package com.albatros.kplanner.domain.usecase.datatransfer.input

data class ServerInputUseCases(
    val createUser: CreateUserUseCase,
    val createUsersSchedule: CreateUsersScheduleUseCase,
    val saveSchedule: SaveScheduleUseCase,
    val saveUser: SaveUserUseCase
)