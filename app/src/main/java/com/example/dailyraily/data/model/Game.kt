package com.example.dailyraily.data.model

import TodoListWithPriority
import android.content.Context
import android.util.Log
import com.example.dailyraily.data.dto.GameCreateDTO
import com.example.dailyraily.data.dto.GameUpdateDTO
import com.example.dailyraily.data.repository.GameDAO
import com.example.dailyraily.data.service.TodoListManager
import com.example.dailyraily.ui.list.ItemDTO
import com.example.dailyraily.ui.list.Listable
import com.example.dailyraily.ui.list.dowToString
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

class Game(
    val name: String,
    var resetDay: Int,
    var resetDOW: DayOfWeek = DayOfWeek.MONDAY,
    var resetHour: Int,
) : Listable {
    init {
        TodoListManager.registerGame(this)
    }

    private val _todoList: HashMap<UUID, Todo> = HashMap()
    val todoList: List<Todo>
        get() = getAllTodo()
    val sortedTodoList: List<Todo>
        get() = TodoListWithPriority.make(todoList).sortedTodoList

    override fun toListItem(): ItemDTO {
        val sortedTodoList = TodoListWithPriority.make(_todoList.values.toList()).sortedTodoList
        val thirdColumn =
            sortedTodoList.getOrNull(0)?.let { "${it.name} ${it.leftTimeString()}" } ?: ""


        return ItemDTO(
            "${name} [${countDoneTodo}/${countTodo}]",
            "${resetHour}시 / ${dowToString(resetDOW)} / ${resetDay}일",
            thirdColumn,
            name,
            ItemDTO.ItemType.GAME
        )
    }

    override fun getId(): String {
        return name
    }

    fun getTodo(uuid: UUID): Todo? {
        return _todoList[uuid]
    }

    fun getTodoPrioritySum(int: Int): Int {
        var result = 0
        for (i in 0 until minOf(sortedTodoList.size, 3)) {
            result += sortedTodoList[i].priority
        }
        return result
    }

    fun getAllTodo(): List<Todo> {
        return _todoList.values.toList()
    }

    val countTodo: Int
        get() {
            return _todoList.size
        }

    val countDoneTodo: Int
        get() {
            return _todoList.values.count { it.done }
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
        this._todoList[todo.uuid] = todo
    }

    fun deregister(uuid: UUID) {
        _todoList.remove(uuid)
    }

    companion object {
        fun create(context: Context, dto: GameCreateDTO) {
            val dao = GameDAO(context)
            val resetDay = if (dto.resetDay == 0) 1 else dto.resetDay
            dao.insertGame(Game(dto.name, resetDay, dto.resetDOW, dto.resetHour))
        }
    }

    fun update(context: Context, dto: GameUpdateDTO) {
        this.resetDOW = dto.resetDOW
        this.resetHour = dto.resetHour
        this.resetDay = dto.resetDay
        updateDB(context)
    }

    fun remove(context: Context) {
        val dao = GameDAO(context)
        _todoList.values.forEach { it.remove(context) }
        TodoListManager.deregisterGame(this.name)
        dao.deleteGame(name)
    }


    private fun updateDB(context: Context) {
        val dao = GameDAO(context)
        dao.updateGame(this)
    }

    override fun toString(): String {
        return name
    }


}


class Games {
    private val games = HashMap<String, Game>()
    fun register(game: Game) {
        this.games[game.name] = game
    }

    fun getAllGame(): List<Game> {
        return games.values.toList()
    }

    fun deregister(name: String) {
        this.games.remove(name)
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
