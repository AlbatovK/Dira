package com.albatros.kplanner.model

import com.google.firebase.auth.FirebaseUser

sealed class EnterResult {
    data class EntrySuccess(val user: FirebaseUser) : EnterResult()
    data class EntryFailure(val exception: Exception) : EnterResult()
    object EntryInvalid : EnterResult()
    object EntryStarted: EnterResult()
}