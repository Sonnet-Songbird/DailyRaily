package com.example.dailyraily.data.dto

import java.time.DayOfWeek

data class GameCreateDTO(
    val name: String,
    val resetDay: Int,
    val resetDOW: DayOfWeek,
    val resetHour: Int,
)

data class GameUpdateDTO(
    val name: String,
    val resetDay: Int,
    val resetDOW: DayOfWeek,
    val resetHour: Int,
)

data class GameRemoveDTO(
    val name: String
)