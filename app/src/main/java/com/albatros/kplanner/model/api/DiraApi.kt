package com.albatros.kplanner.model.api

import com.albatros.kplanner.model.data.DiraNote
import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.model.data.NotesIdsList
import com.albatros.kplanner.model.data.Schedule
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DiraApi {

    @POST(value = "/user/create")
    suspend fun createUser(@Body user: DiraUser): DiraUser

    @GET(value = "/user/find")
    suspend fun findUserByToken(@Query("user_id") user_id: String): DiraUser

    @GET(value = "/user/get")
    suspend fun getUsers(@Query("from") from: Int, @Query("to") to: Int): List<DiraUser>

    @GET(value = "/user/get/all")
    suspend fun getAllUsers(): List<DiraUser>

    @GET(value = "/user/league/get")
    suspend fun getUsersByLeague(@Query("league") league: Int): List<DiraUser>

    @POST(value = "/note/add")
    suspend fun addNotes(@Body notesIdList: NotesIdsList, @Query("user_id") user_id: String)

    @GET(value = "/note/get")
    suspend fun getNotes(@Query("from") from: Int, @Query("to") to: Int): List<DiraNote>

    @GET(value = "/note/get/all")
    suspend fun getAllNotes(): List<DiraNote>

    @POST(value = "/schedule/create")
    suspend fun createSchedule(@Body schedule: Schedule)

    @GET(value = "/schedule/get")
    suspend fun getScheduleByOwnerId(@Query("owner_id") owner_id: String): Schedule

}