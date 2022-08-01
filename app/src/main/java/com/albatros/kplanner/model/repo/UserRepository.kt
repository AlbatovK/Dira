package com.albatros.kplanner.model.repo

import com.albatros.kplanner.model.data.DiraUser
import com.albatros.kplanner.model.data.Schedule
import com.albatros.kplanner.model.network.DiraApi

class UserRepository(private val api: DiraApi) {

    var diraUser: DiraUser? = null
    var schedule: Schedule? = null

    fun getCurrentUser() = diraUser!!

    fun getCurrentSchedule() = schedule!!

    suspend fun saveUser(user: DiraUser) {
        api.createUser(user)
        this.diraUser = user
    }

    suspend fun saveSchedule(schedule: Schedule) {
        api.createSchedule(schedule)
        this.schedule = schedule
    }

    suspend fun loadUser(userFirebaseId: String): DiraUser =
        api.findUserByToken(userFirebaseId).also { diraUser = it }

    suspend fun loadSchedule(userFirebaseId: String): Schedule =
        api.getScheduleByOwnerId(userFirebaseId).also { schedule = it }

}