package com.albatros.kplanner.model.module

import com.albatros.kplanner.model.repo.*
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val repoModule = module {
    single { PreferencesRepository(get()) }
    single { UserRepository(get()) }
    single { NoteRepository(get(), get()) }
    single { ConnectivityRepositoryImpl(androidContext()) } bind ConnectivityRepository::class
}