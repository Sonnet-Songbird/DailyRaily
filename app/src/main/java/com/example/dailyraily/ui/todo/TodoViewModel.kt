package com.example.dailyraily.ui.todo

import androidx.lifecycle.ViewModel
import com.example.dailyraily.data.model.Todo
import com.example.dailyraily.data.service.TodoListManager

class TodoViewModel : ViewModel() {
    var data: List<Todo> = listOf()

    fun updateData() {
        data = TodoListManager.getAllTodosWithPriority()
    }
}