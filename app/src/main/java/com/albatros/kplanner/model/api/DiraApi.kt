package com.albatros.kplanner.model.api

import com.albatros.kplanner.model.data.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DiraApi {

    @GET(value = "/user/league/get/all")
    suspend fun getUsersByLeague(@Query("league") league: Int): List<DiraUser>

    @GET(value = "/user/get/all")
    suspend fun getAllUsers(): List<DiraUser>

    @POST(value = "/note/schedule/add/many")
    suspend fun addNotes(@Body noteList: NotesList, @Query("user_id") user_id: String)

    @GET(value = "/session/enable")
    suspend fun preActivate()

    @POST(value = "/user/create")
    suspend fun createUser(@Body user: DiraUser): DiraUser

    @GET(value = "/user/find")
    suspend fun findUserByToken(@Query("user_id") user_id: String): DiraUser

    @GET(value = "/note/get/all")
    suspend fun getAllNotes(): List<DiraNote>

    @GET(value = "/note/schedule/get")
    suspend fun getScheduleByOwnerId(@Query("owner_id") owner_id: String): Schedule

    @POST(value = "/note/schedule/create")
    suspend fun createSchedule(@Body schedule: Schedule)

    @GET(value = "/note/schedule/add")
    suspend fun addNote(@Query("note_id") note_id: Long, @Query("user_id") user_id: String): Boolean

}