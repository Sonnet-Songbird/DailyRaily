package com.example.dailyraily.data.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "games",
)
class Game(
    @PrimaryKey val name: String,
    val resetDOW: DayOfWeek,
    val resetHour: Int,
    val resetDay: Int,
    val todoList: ArrayList<Todo> = ArrayList(),
) {
    @get:Ignore
    val adjustedDate: LocalDate
        get() {
            var now = LocalDateTime.now()
            if (resetHour != 0) {
                if (now.hour < resetHour) {
                    now = now.minusDays(1)
                }
            }
            return now.toLocalDate()
        }


    fun register(todo: Todo) {
        this.todoList.add(todo)
    }

    fun register(todos: Array<Todo>) {

    }
}

class GameList(private val gameList: Array<Game>)
