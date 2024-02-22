package com.example.dailyraily.data.service

import TodoCreateDTO
import TodoRemoveDTO
import TodoUpdateDTO
import android.content.Context
import android.provider.SyncStateContract.Helpers.update
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

    fun resetTodo(context: Context, todo: Todo) {
        todo.reset(context)
    }

    fun removeTodo(context: Context, dto: TodoRemoveDTO) {
        getTodo(getGame(dto.gameName), dto.uuid).remove(context)
    }

}
