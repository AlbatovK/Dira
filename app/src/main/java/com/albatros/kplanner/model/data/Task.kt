package com.albatros.kplanner.model.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    @SerializedName("title")
    @Expose
    var title: String = "",
    @SerializedName("finished")
    @Expose
    var finished: Boolean = false,
) : Parcelable