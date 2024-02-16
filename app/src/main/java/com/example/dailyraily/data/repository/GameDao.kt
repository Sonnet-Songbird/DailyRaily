//Todo: 테스트 가능한 시점에 다시 Room 적용

package com.example.dailyraily.data.repository

import androidx.room.Dao
import androidx.room.Query
import com.example.dailyraily.data.model.Game
import com.example.dailyraily.data.model.Todo

@Dao
interface GameDao {
    @Query(
        "SELECT * FROM games " +
                "JOIN todos ON name = gameName"
    )
    fun loadUserAndBookNames(): Map<Game, List<Todo>>
}