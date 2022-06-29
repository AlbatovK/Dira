package com.albatros.kplanner.model.module

import com.albatros.kplanner.ui.fragments.enter.EnterViewModel
import com.albatros.kplanner.ui.fragments.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { EnterViewModel(get(), get()) }
    viewModel { RegisterViewModel(get(), get()) }
}