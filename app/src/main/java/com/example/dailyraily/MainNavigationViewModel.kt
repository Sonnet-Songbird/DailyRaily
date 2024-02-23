package com.example.dailyraily

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.dailyraily.data.model.Game
import com.example.dailyraily.data.model.Todo
import com.example.dailyraily.data.service.ExceptionHandler
import com.example.dailyraily.data.service.TodoListManager
import com.example.dailyraily.ui.list.HeaderTodoDTO

class MainNavigationViewModel : ViewModel() {
    private lateinit var headerData: Todo
    val navData: ArrayDeque<Game> = ArrayDeque()
    fun updateNavigation(context: Context) {
        updateHeader(context)
    }

    fun updateHeader(context: Context) {
        try {
            headerData = TodoListManager.getMostPriorityTodo()
        } catch (e: IndexOutOfBoundsException) {
            ExceptionHandler.noRegisterTodo()
        }

    }

    fun initNav(context: Context, itemCount: Int) {
        val games = TodoListManager.getMostPriorityGame(itemCount, 3)
        navData.addAll(games)
    }


    fun getHeader(): HeaderTodoDTO {
        if (headerData != null) {
            val resetType = headerData.resetType.name
            val name = headerData.name
            val gameName = headerData.game.name
            val leftTime = headerData.leftTimeString()
            val count = headerData.count
            val goal = headerData.goal


            return HeaderTodoDTO(
                "[${resetType}] ${name}",
                "${gameName} ${count}/${goal}",
                "${leftTime}",
                headerData.uuid.toString()
            )
        } else {
            return HeaderTodoDTO(
                "등록된 Todo가 없습니다.",
                "",
                "",
                ""
            )
        }

    }

    fun getItemName(index: Int): String {
        return navData.elementAt(index).name
    }


}