package com.albatros.kplanner.model.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Schedule(

    @SerializedName("ownerId")
    @Expose
    val ownerId: String = "",

    @SerializedName("tasks")
    @Expose
    var tasks: MutableList<DiraNote> = mutableListOf(),

    @SerializedName("id")
    @Expose
    var id: Long = UUID.randomUUID().mostSignificantBits

) : Parcelable