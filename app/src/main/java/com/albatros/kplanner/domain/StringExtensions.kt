package com.albatros.kplanner.domain

fun isEntryValid(vararg args: String?) =
    args.none { it.isNullOrBlank() or it.isNullOrEmpty() }

fun getLeague(league: Int) = leagueMap.getOrDefault(league, "Неизвестная лига")

val leagueMap = mapOf(
    1 to "Бронзовая лига",
    2 to "Серебряная лига",
    3 to "Золотая лига",
    4 to "Платиновая лига",
    5 to "Алмазная лига",
)

val monthMap = mapOf(
    0 to "января",
    1 to "февраля",
    2 to "марта",
    3 to "апреля",
    4 to "мая",
    5 to "июня",
    6 to "июля",
    7 to "августа",
    8 to "сентября",
    9 to "октября",
    10 to "ноября",
    11 to "декабря",
)


fun getMonth(pos: Int) = monthMap.getOrDefault(pos, "января")