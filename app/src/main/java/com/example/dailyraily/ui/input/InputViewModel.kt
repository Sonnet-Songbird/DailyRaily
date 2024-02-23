//Todo 추상화 적용 필요.
package com.example.dailyraily.ui.input

import TodoCreateDTO
import TodoUpdateDTO
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.dailyraily.data.dto.GameCreateDTO
import com.example.dailyraily.data.dto.GameUpdateDTO
import com.example.dailyraily.data.model.Game
import com.example.dailyraily.data.model.ResetType
import com.example.dailyraily.data.service.TodoListManager
import java.time.DayOfWeek
import java.util.UUID

class InputViewModel : ViewModel() {

    fun register(isEdit: Boolean, context: Context, dto: TodoInputDTO) {
        if (isEdit) {
            update(context, dto)
        } else {
            create(context, dto)

        }
    }

    fun register(isEdit: Boolean, context: Context, dto: GameInputDTO) {
        if (isEdit) {
            update(context, dto)
        } else {
            create(context, dto)

        }
    }

    private fun create(context: Context, dto: TodoInputDTO) {
        TodoListManager.createTodo(
            context, TodoCreateDTO(
                dto.selectedGame,
                dto.enteredName,
                dto.enteredGoal.toInt(),
                ResetType.valueOf(dto.selectedResetType),
                dto.isImportant.toBoolean()
            )

        )


    }

    private fun create(context: Context, dto: GameInputDTO) {
        TodoListManager.createGame(
            context,
            GameCreateDTO(
                dto.enteredName,
                dto.enteredDate.toInt(),
                DayOfWeek.valueOf(dto.selectedDayOfWeek),
                dto.enteredTime.toInt()
            )
        )
        Log.d("test", "INP View: ${dto.selectedDayOfWeek}")

    }

    private fun update(context: Context, dto: TodoInputDTO) {
        TodoListManager.updateTodo(
            context,
            TodoUpdateDTO(
                dto.selectedGame,
                UUID.fromString(dto.uuidNullable),
                dto.enteredName,
                dto.enteredGoal.toInt(),
                ResetType.valueOf(dto.selectedResetType),
                dto.isImportant.toBoolean()
            )
        )
    }

    private fun update(context: Context, dto: GameInputDTO) {
        TodoListManager.updateGame(
            context,
            GameUpdateDTO(
                dto.enteredName,
                dto.enteredDate.toInt(),
                DayOfWeek.valueOf(dto.selectedDayOfWeek),
                dto.enteredTime.toInt()
            )
        )
    }

}

data class TodoInputDTO(
    val selectedGame: String,
    val enteredName: String,
    val selectedResetType: String,
    val enteredGoal: String,
    val isImportant: String,
    val uuidNullable: String
)

data class GameInputDTO(
    val enteredName: String,
    val selectedDayOfWeek: String,
    val enteredTime: String,
    val enteredDate: String
)