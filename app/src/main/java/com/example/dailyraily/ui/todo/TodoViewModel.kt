package com.example.dailyraily.ui.todo

import androidx.lifecycle.ViewModel
import com.example.dailyraily.data.model.Todo

class TodoViewModel : ViewModel() {
    val data: MutableList<Todo> = mutableListOf()
}