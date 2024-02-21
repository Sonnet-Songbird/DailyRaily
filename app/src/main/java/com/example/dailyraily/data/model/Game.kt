package com.example.dailyraily.data.model

import android.content.Context
import com.example.dailyraily.data.repository.GameDAO
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class Game(
    val name: String,
    val resetDOW: DayOfWeek,
    val resetHour: Int,
    val resetDay: Int,
) {
    private val todoList: HashMap<UUID, Todo> = HashMap()


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
        this.todoList[todo.uuid] = todo
    }

    fun register(todos: Array<Todo>) {
        todos.forEach { v -> register(v) }
    }
}


class Games {
    private val games = HashMap<String, Game>()

    fun loadAllGames(context: Context) {
        val gameQuery = GameDAO(context).getAllGames()
        gameQuery.forEach { game ->
            put(game.name, game)
        }
        Todo.loadAllTodos(context)
    }

    fun get(name: String): Game? {
        return games[name]
    }

    fun put(name: String, game: Game) {
        games.putIfAbsent(game.name, game)
    }
}
