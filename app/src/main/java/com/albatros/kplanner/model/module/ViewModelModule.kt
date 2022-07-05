package com.albatros.kplanner.model.module

import com.albatros.kplanner.ui.fragments.enter.EnterViewModel
import com.albatros.kplanner.ui.fragments.list.ListViewModel
import com.albatros.kplanner.ui.fragments.main.MainViewModel
import com.albatros.kplanner.ui.fragments.register.RegisterViewModel
import com.albatros.kplanner.ui.fragments.stats.StatsViewModel
import com.albatros.kplanner.ui.fragments.users.UsersListViewModel
import com.albatros.kplanner.ui.fragments.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EnterViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
    viewModel { WelcomeViewModel(get()) }
    viewModel { ListViewModel(get(), get()) }
    viewModel { MainViewModel(get(), get()) }
    viewModel { StatsViewModel(get(), get()) }
    viewModel { UsersListViewModel(get(), get()) }
}