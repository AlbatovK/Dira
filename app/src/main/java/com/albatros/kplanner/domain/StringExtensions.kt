package com.albatros.kplanner.domain

fun isEntryValid(vararg args: String?) =
    args.none { it.isNullOrBlank() or it.isNullOrEmpty() }