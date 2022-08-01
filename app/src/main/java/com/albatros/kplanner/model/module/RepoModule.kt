package com.albatros.kplanner.model.module

import com.albatros.kplanner.model.repo.NoteRepository
import com.albatros.kplanner.model.repo.PreferencesRepository
import com.albatros.kplanner.model.repo.UserRepository
import org.koin.dsl.module

val repoModule = module {
    single { PreferencesRepository(get()) }
    single { UserRepository(get()) }
    single { NoteRepository(get(), get()) }
}