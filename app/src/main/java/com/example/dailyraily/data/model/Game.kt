package com.example.dailyraily.data.model

import android.content.Context
import com.example.dailyraily.data.dto.GameCreateDTO
import com.example.dailyraily.data.repository.GameDAO
import com.example.dailyraily.ui.list.ItemData
import com.example.dailyraily.ui.list.Listable
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class Game(
    val name: String,
    val resetDay: Int,
    val resetDOW: DayOfWeek,
    val resetHour: Int,
) : Listable {
    init {
        TodoListManager.registerGame(this)
    }

    private val todoList: HashMap<UUID, Todo> = HashMap()
    override fun toListItem(): ItemData {
        TODO("Not yet implemented")
    }

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

    companion object {
        fun create(context: Context, dto: GameCreateDTO) {
            Game(dto.name, dto.resetDay, dto.resetDOW, dto.resetHour)
        }
    }
}


class Games {
    private val games = HashMap<String, Game>()
    fun register(game: Game) {
        this.games[game.name] = game
    }

    fun loadAllGames(context: Context) {
        val gameQuery = GameDAO(context).getAllGames()
        gameQuery.forEach { game ->
            put(game.name, game)
        }
    }

    fun get(name: String): Game? {
        return games[name]
    }

    fun put(name: String, game: Game) {
        games.putIfAbsent(game.name, game)
    }

}
