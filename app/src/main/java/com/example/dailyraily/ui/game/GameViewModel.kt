package com.example.dailyraily.ui.game

import androidx.lifecycle.ViewModel
import com.example.dailyraily.data.model.Game
import com.example.dailyraily.data.service.TodoListManager

class GameViewModel : ViewModel() {
    var data: List<Game> = listOf()


    fun updateData() {
        data = TodoListManager.getAllGame()
    }
}