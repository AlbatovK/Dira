package com.albatros.kplanner.model.repo

import android.app.Application
import com.albatros.kplanner.model.module.appModule
import com.albatros.kplanner.model.module.repoModule
import com.albatros.kplanner.model.module.useCaseModule
import com.albatros.kplanner.model.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DiraApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val androidContext = this

        val modules = listOf(
            appModule,
            repoModule,
            useCaseModule,
            viewModelModule
        )

        startKoin {
            androidLogger()
            androidContext(androidContext)
            modules(modules)
        }
    }
}
