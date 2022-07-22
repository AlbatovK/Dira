package com.albatros.kplanner.model.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotesIdsList(

    @SerializedName("notes")
    @Expose
    val notes: List<Long> = listOf()

) : Parcelable