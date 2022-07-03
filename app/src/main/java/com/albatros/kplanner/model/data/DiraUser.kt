package com.albatros.kplanner.model.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiraUser(

    @SerializedName("tokenId")
    @Expose
    var tokenId: String = "",

    @SerializedName("score")
    @Expose
    var score: Long = 0,

    @SerializedName("email")
    @Expose
    var email: String = "",

    @SerializedName("nickname")
    @Expose
    var nickname: String = "",

    @SerializedName("friendsIds")
    @Expose
    var friendsIds: List<String> = listOf(),

    @SerializedName("league")
    @Expose
    var league: Int = 1,

    @SerializedName("scoreOfDay")
    @Expose
    var scoreOfDay: Int = 0,

    @SerializedName("scoreOfWeek")
    @Expose
    var scoreOfWeek: Int = 0,

) : Parcelable