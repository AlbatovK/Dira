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
    @SerializedName("score")
    @Expose
    var score: Int = 0,
    @SerializedName("id")
    @Expose
    var id: Long = UUID.randomUUID().mostSignificantBits,
    @SerializedName("finished")
    @Expose
    var finished: Boolean = false
) : Parcelable