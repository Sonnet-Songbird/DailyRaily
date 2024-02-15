package com.example.dailyraily.data.model

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID


class Game(
    val name: String,
    val resetDOW: DayOfWeek,
    val resetHour: Int,
    val todoList: ArrayList<Todo> = ArrayList(),
    val todoIdList: ArrayList<UUID> = ArrayList()
) {
    fun register(todo: Todo) {
        this.todoList.add(todo)
        this.todoIdList.add(todo.todoID)
    }

    fun adjustedDate(): LocalDate {
        var now = LocalDateTime.now()
        if (resetHour != 0) {
            if (now.hour < resetHour) {
                now = now.minusDays(1)
            }
        }
        return now.toLocalDate()
    }

}

class GameList(private val gameList: Array<Game>)
