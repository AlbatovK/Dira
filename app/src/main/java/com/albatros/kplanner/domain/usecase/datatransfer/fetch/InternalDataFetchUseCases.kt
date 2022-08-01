package com.albatros.kplanner.domain.usecase.datatransfer.fetch

data class InternalDataFetchUseCases(
    val loadUser: LoadInternalUserUseCase,
    val loadUsersSchedule: LoadUsersScheduleUseCase
)