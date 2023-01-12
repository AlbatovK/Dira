package com.albatros.kplanner.model.module

import com.albatros.kplanner.ui.activity.MainActivityViewModel
import com.albatros.kplanner.ui.fragments.drawer.DrawerViewModel
import com.albatros.kplanner.ui.fragments.enter.EnterViewModel
import com.albatros.kplanner.ui.fragments.list.ListViewModel
import com.albatros.kplanner.ui.fragments.main.MainViewModel
import com.albatros.kplanner.ui.fragments.navigation.NavigationViewModel
import com.albatros.kplanner.ui.fragments.profile.ProfileViewModel
import com.albatros.kplanner.ui.fragments.register.RegisterViewModel
import com.albatros.kplanner.ui.fragments.stats.StatsViewModel
import com.albatros.kplanner.ui.fragments.users.UsersListViewModel
import com.albatros.kplanner.ui.fragments.welcome.WelcomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EnterViewModel(get(), get(), get(), get()) }
    viewModel { RegisterViewModel(get(), get(), get(), get()) }
    viewModel { WelcomeViewModel(get()) }
    viewModel { ListViewModel(get()) }
    viewModel { MainViewModel(get(), get(), get(), get(), get()) }
    viewModel { StatsViewModel(get(), get()) }
    viewModel { NavigationViewModel() }
    viewModel { MainActivityViewModel(get(), get()) }
    viewModel { UsersListViewModel(get(), get(), get(), get(), get()) }
    viewModel { parameters -> ProfileViewModel(get(), get(), parameters.get()) }
    viewModel { DrawerViewModel(get(), get(), get()) }
}