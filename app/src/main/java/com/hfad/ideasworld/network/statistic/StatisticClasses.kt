package com.hfad.ideasworld.network.statistic

data class Statistic(
    val magnitudeCount: MagnitudeCount,
    val rate: Rate
)

data class MagnitudeCount(
    val days365: Map<String,Int>,
    val days28: Map<String,Int>,
    val days7: Map<String,Int>
)

data class Rate(
    val perDay: Map<String,Int>
)
