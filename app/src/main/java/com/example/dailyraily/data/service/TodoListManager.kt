package com.example.dailyraily.data.service

import TodoCreateDTO
import android.content.Context
import com.example.dailyraily.data.dto.GameCreateDTO
import com.example.dailyraily.data.model.Game
import com.example.dailyraily.data.model.Games
import com.example.dailyraily.data.model.Todo

object TodoListManager {

    private val games: Games = Games()
    fun load(context: Context) {
        games.loadAllGames(context)
        Todo.loadAllTodos(context)

    }

    fun getGame(name: String): Game? {
        return games.get(name)
    }

    fun registerGame(game: Game) {
        games.register(game)
    }

    fun resetTodo(context: Context, todo: Todo) {
        todo.reset(context)
    }

    fun createTodo(context: Context, dto: TodoCreateDTO) {
        Todo.create(context, dto)
    }

    fun createGame(context: Context, dto: GameCreateDTO) {
        Game.create(context, dto)
    }


}