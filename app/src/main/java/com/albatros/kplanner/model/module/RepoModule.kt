package com.albatros.kplanner.model.module

import com.albatros.kplanner.model.repo.UserRepo
import org.koin.dsl.module

val repoModule = module {
    single { UserRepo() }
}