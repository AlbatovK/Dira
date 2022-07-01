package com.albatros.kplanner.model.api

import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.data.DiraUser
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DiraApi {

    @GET(value = "/session/enable")
    suspend fun preActivate()

    @POST(value = "/user/create")
    suspend fun createUser(@Body user: DiraUser): DiraUser

    @GET(value = "/user/find")
    suspend fun findUserByToken(@Query("user_id") user_id: String): DiraUser

    @GET(value = "/note/get/all")
    suspend fun getAllNotesByOwnerId(@Query("user_id") user_id: String): List<DiraNote>

    @POST(value = "/note/create")
    suspend fun createNote(@Body note: DiraNote)
}