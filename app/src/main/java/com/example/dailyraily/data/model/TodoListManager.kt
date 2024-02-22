package com.example.dailyraily.data.model

import TodoCreateDTO
import android.content.Context
import com.example.dailyraily.data.dto.GameCreateDTO

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

    fun creatTodo(context: Context, dto: TodoCreateDTO) {
        Todo.create(context, dto)
    }


}