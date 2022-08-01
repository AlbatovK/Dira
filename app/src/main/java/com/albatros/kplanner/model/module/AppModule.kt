package com.albatros.kplanner.model.module

import android.content.Context
import com.albatros.kplanner.model.network.DiraApi
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val host_name = "https://diraserver.herokuapp.com"

private const val settingsName = "settings"

private fun provideApiService(retrofit: Retrofit) =
    retrofit.create(DiraApi::class.java)

private fun provideRetrofit(factory: GsonConverterFactory) = Retrofit.Builder()
    .baseUrl(host_name)
    .addConverterFactory(factory)
    .build()

private fun provideGsonFactory(gson: Gson) =
    GsonConverterFactory.create(gson)

private fun provideGson() = GsonBuilder()
    .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
    .setPrettyPrinting()
    .serializeNulls()
    .create()

private fun provideSharedPreferences(context: Context) =
    context.getSharedPreferences(settingsName, Context.MODE_PRIVATE)

private fun provideFirebaseAnalytics(context: Context) =
    FirebaseAnalytics.getInstance(context)

val appModule = module {
    single(createdAtStart = true) { provideSharedPreferences(get()) }
    single(createdAtStart = true) { provideFirebaseAnalytics(get()) }
    single(createdAtStart = true) { provideApiService(get()) }
    single(createdAtStart = true) { provideRetrofit(get()) }
    single(createdAtStart = true) { provideGsonFactory(get()) }
    single(createdAtStart = true) { provideGson() }
}