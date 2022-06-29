package com.albatros.kplanner.model.repo

import android.app.Application
import com.albatros.kplanner.model.module.appModule
import com.albatros.kplanner.model.module.repoModule
import com.albatros.kplanner.model.module.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Suppress("unused")
class DiraApp : Application() {

    private val modules = listOf(appModule, repoModule, viewModelModule)

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DiraApp)
            modules(modules)
        }
    }
}
