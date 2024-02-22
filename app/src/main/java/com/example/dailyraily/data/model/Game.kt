package com.example.dailyraily.data.model

import TodoListWithPriority
import android.content.Context
import com.example.dailyraily.data.dto.GameCreateDTO
import com.example.dailyraily.data.repository.GameDAO
import com.example.dailyraily.data.service.TodoListManager
import com.example.dailyraily.ui.list.ItemDTO
import com.example.dailyraily.ui.list.ListAdapter
import com.example.dailyraily.ui.list.Listable
import com.example.dailyraily.ui.list.dowToString
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
    override fun toListItem(): ItemDTO {
        val todo = TodoListWithPriority.make(todoList.values.toList()).sortedTodoList[0]
        val thirdColumn = todo.let { "${it.name} ${it.leftTimeString()}" }

        return ItemDTO(
            "${name} [${countDoneTodo}/{$countTodo}]",
            "${resetHour}시 / ${dowToString(resetDOW)} / ${resetDay}일",
            thirdColumn,
            name
        )
    }


    val countTodo: Int
        get() {
            return todoList.size
        }

    val countDoneTodo: Int
        get() {
            return todoList.values.count { it.done }
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

    companion object {
        fun create(context: Context, dto: GameCreateDTO) {
            val dao = GameDAO(context)
            dao.insertGame(Game(dto.name, dto.resetDay, dto.resetDOW, dto.resetHour))
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
