package com.example.dailyraily.data.model

import TodoListWithPriority
import com.example.dailyraily.data.service.TodoListManager
import java.util.UUID

class Filter(val keys: List<Pair<String, UUID>>) {
    val todoList: TodoListWithPriority
        get() {
            val list: ArrayList<Todo> = ArrayList()
            for (pair in keys) {
                list.add(
                    TodoListManager.getTodo(
                        TodoListManager.getGame(pair.first),
                        pair.second
                    )
                )
            }
            return TodoListWithPriority.make(list)
        }
}