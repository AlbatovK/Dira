package com.albatros.kplanner.model.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.util.*

@Parcelize
data class DiraNote(
    @SerializedName("title")
    @Expose
    var title: String = "",
    @SerializedName("description")
    @Expose
    var description: String = "",
    @SerializedName("tasks")
    @Expose
    var tasks: @RawValue List<Task> = listOf(),
    @SerializedName("date")
    @Expose
    var date: String = Date().toString(),
    @SerializedName("finished")
    @Expose
    var finished: Boolean = false,
    @SerializedName("id")
    @Expose
    var id: Long = UUID.randomUUID().mostSignificantBits,
    @SerializedName("ownerId")
    @Expose
    var ownerId: String = ""
) : Parcelable