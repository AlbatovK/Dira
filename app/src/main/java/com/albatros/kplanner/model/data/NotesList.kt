package com.albatros.kplanner.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NotesList(
    @SerializedName("notes")
    @Expose
    val noteIds: List<DiraNote> = listOf()
)