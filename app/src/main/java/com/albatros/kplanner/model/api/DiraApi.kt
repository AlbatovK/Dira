package com.albatros.kplanner.model.api

import com.albatros.kplanner.model.DiraUser
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DiraApi {

    @POST(value = "/user/create")
    suspend fun createUser(@Body user: DiraUser): DiraUser

    @GET(value = "/user/find")
    suspend fun findUserByToken(@Query("user_id") user_id: String): DiraUser
}