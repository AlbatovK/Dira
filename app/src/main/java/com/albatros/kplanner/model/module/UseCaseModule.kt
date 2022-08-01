package com.albatros.kplanner.model.module

import com.albatros.kplanner.domain.usecase.auth.AuthUseCases
import com.albatros.kplanner.domain.usecase.auth.SignInFirebaseUseCase
import com.albatros.kplanner.domain.usecase.auth.SignUpFirebaseUseCase
import com.albatros.kplanner.domain.usecase.autoenter.AutoEnterUseCases
import com.albatros.kplanner.domain.usecase.autoenter.GetAutoEnterDataUseCase
import com.albatros.kplanner.domain.usecase.autoenter.SetAutoEnterDataUseCase
import com.albatros.kplanner.domain.usecase.datatransfer.fetch.*
import com.albatros.kplanner.domain.usecase.datatransfer.get.GetCurrentScheduleUseCase
import com.albatros.kplanner.domain.usecase.datatransfer.get.GetCurrentUserUseCase
import com.albatros.kplanner.domain.usecase.datatransfer.input.*
import com.albatros.kplanner.domain.usecase.note.*
import com.albatros.kplanner.domain.usecase.social.AddFriendsUseCase
import com.albatros.kplanner.domain.usecase.social.LoadAllUsersUseCase
import com.albatros.kplanner.domain.usecase.social.LoadFriendsUseCase
import com.albatros.kplanner.domain.usecase.social.LoadUsersByNamePrefixUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { AuthUseCases(get(), get()) }
    single { SignInFirebaseUseCase() }
    single { SignUpFirebaseUseCase() }
    single { AutoEnterUseCases(get(), get()) }
    single { GetAutoEnterDataUseCase(get()) }
    single { SetAutoEnterDataUseCase(get()) }
    single { InternalDataFetchUseCases(get(), get()) }
    single { LoadInternalUserUseCase(get()) }
    single { LoadUsersScheduleUseCase(get()) }
    single { LoadAllNotesUseCase(get()) }
    single { RemoveNoteAtFromScheduleUseCase(get()) }
    single { ChangeNotePositionUseCase(get()) }
    single { AddNotesToScheduleByIdUseCase(get()) }
    single { AddNotesToScheduleUseCase(get()) }
    single { CreateUsersScheduleUseCase(get()) }
    single { CreateUserUseCase(get()) }
    single { ServerInputUseCases(get(), get(), get(), get()) }
    single { GetCurrentUserUseCase(get()) }
    single { GetCurrentScheduleUseCase(get()) }
    single { SaveScheduleUseCase(get()) }
    single { SaveUserUseCase(get()) }
    single { FinishNoteUseCase(get()) }
    single { ImportScheduleUseCase(get(), get(), get()) }
    single { LoadAllByUsersLeagueUseCase(get(), get()) }
    single { NotesUseCases(get(), get(), get(), get(), get(), get()) }
    single { AddFriendsUseCase(get()) }
    single { LoadAllUsersUseCase(get(), get()) }
    single { LoadFriendsUseCase(get(), get()) }
    single { LoadUsersByNamePrefixUseCase(get()) }
}