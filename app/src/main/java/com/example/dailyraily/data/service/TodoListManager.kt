package com.example.dailyraily.data.service

import TodoCreateDTO
import TodoListWithPriority
import TodoRemoveDTO
import TodoUpdateDTO
import android.content.Context
import android.system.Os.remove
import android.util.Log
import com.example.dailyraily.data.dto.GameCreateDTO
import com.example.dailyraily.data.dto.GameRemoveDTO
import com.example.dailyraily.data.dto.GameUpdateDTO
import com.example.dailyraily.data.model.Game
import com.example.dailyraily.data.model.Games
import com.example.dailyraily.data.model.Todo
import java.util.UUID

object TodoListManager {

    private val games: Games = Games()
    fun load(context: Context) {
        games.loadAllGames(context)
        Todo.loadAllTodos(context)
    }


    fun registerGame(game: Game) {
        games.register(game)
    }

    fun deregisterGame(name: String) {
        games.deregister(name)
    }

    fun getGame(name: String): Game {
        return games.get(name) ?: throw IllegalArgumentException("No Such Game ${name}")
    }

    fun getTodo(game: Game, uuid: UUID): Todo {
        return game.getTodo(uuid) ?: throw IllegalArgumentException("No Such Todo ${uuid}")
    }

    fun getAllGame(): List<Game> {
        return games.getAllGame()
    }

    fun createGame(context: Context, dto: GameCreateDTO) {
        Game.create(context, dto)
    }

    fun updateGame(context: Context, dto: GameUpdateDTO) {
        getGame(dto.name).update(context, dto)
    }

    fun removeGame(context: Context, dto: GameRemoveDTO) {
        getGame(dto.name).remove(context)
    }


    fun createTodo(context: Context, dto: TodoCreateDTO) {
        Todo.create(context, dto)
    }

    fun updateTodo(context: Context, dto: TodoUpdateDTO) {
        getTodo(getGame(dto.gameName), dto.uuid).update(context, dto)
    }

    fun removeTodo(context: Context, dto: TodoRemoveDTO) {
        val todo = getTodo(getGame(dto.gameName), UUID.fromString(dto.uuid))
        todo.remove(context)
    }


    fun getMostPriorityTodo(): Todo {
        return getAllTodosWithPriority()[0]
    }

    fun getAllTodosWithPriority(): List<Todo> {
        return TodoListWithPriority.make(getAllTodos()).sortedTodoList
    }

    fun countTodo(context: Context, game: String, uuid: String) {
        getTodo(getGame(game), UUID.fromString(uuid)).count(context)
    }
    fun discountTodo(context: Context, game: String, uuid: String) {
        getTodo(getGame(game), UUID.fromString(uuid)).discount(context)
    }
    fun resetTodo(context: Context, game: String, uuid: String) {
        getTodo(getGame(game), UUID.fromString(uuid)).reset(context)
    }

    private fun getAllTodos(): List<Todo> {
        val todos = ArrayList<Todo>()
        for (game in getAllGame()) {
            for (todo in game.todoList) {
                todos.add(todo)
            }
        }
        return todos.toList()
    }

    fun getMostPriorityGame(pickedGameAmount: Int, checkedTodoAmount: Int): List<Game> {
        return getAllGame().sortedByDescending { it.getTodoPrioritySum(checkedTodoAmount) }
            .take(pickedGameAmount)
    }

    fun getTodos(gameName: String): List<Todo> {
        return getGame(gameName).todoList
    }
}
