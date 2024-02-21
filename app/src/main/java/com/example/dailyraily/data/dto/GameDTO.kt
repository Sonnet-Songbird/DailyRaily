package com.example.dailyraily.data.dto

import java.time.DayOfWeek

data class GameCreateDTO(
    val name: String,
    val resetDOW: DayOfWeek,
    val resetHour: Int,
    val resetDay: Int,
)