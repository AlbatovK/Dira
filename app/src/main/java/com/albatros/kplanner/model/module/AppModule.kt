package com.albatros.kplanner.model.module

import android.content.Context
import com.albatros.kplanner.model.api.DiraApi
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val host_name = "https://secret-escarpment-88160.herokuapp.com"

private const val settingsName = "settings"

private fun provideApiService(retrofit: Retrofit) =
    retrofit.create(DiraApi::class.java)

private fun provideRetrofit(factory: GsonConverterFactory) = Retrofit.Builder()
    .baseUrl(host_name)
    .addConverterFactory(factory)
    .build()

private fun provideFirebaseAnalytics(context: Context) =
    FirebaseAnalytics.getInstance(context)

private fun provideGsonFactory(gson: Gson) =
    GsonConverterFactory.create(gson)

private fun provideGson() = GsonBuilder()
    .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
    .setPrettyPrinting()
    .serializeNulls()
    .create()

private fun provideSharedPreferences(context: Context) =
    context.getSharedPreferences(settingsName, Context.MODE_PRIVATE)

val appModule = module {
    single { provideSharedPreferences(get()) }
    single { provideFirebaseAnalytics(get()) }
    single { provideApiService(get()) }
    single { provideRetrofit(get()) }
    single { provideGsonFactory(get()) }
    single { provideGson() }
}