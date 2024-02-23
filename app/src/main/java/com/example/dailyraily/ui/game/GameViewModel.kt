package com.example.dailyraily.ui.game

import androidx.lifecycle.ViewModel
import com.example.dailyraily.data.model.Game

class GameViewModel : ViewModel() {
    val data: MutableList<Game> = mutableListOf()

}