package com.example.dailyraily.data.model

import TodoCreateDTO
import android.content.Context

object TodoListManager {
    private val games: Games = Games()
    fun load(context: Context) {
        games.loadAllGames(context)
    }

    fun getGame(name: String): Game? {
        return games.get(name)
    }

    fun resetTodo(context: Context, todo: Todo) {
        todo.reset(context)
    }

    fun creatTodo(context: Context, dto: TodoCreateDTO) {
        Todo.create(context, dto)
    }


}