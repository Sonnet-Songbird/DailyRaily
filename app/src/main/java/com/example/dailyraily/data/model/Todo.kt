package com.example.dailyraily.data.model

import TodoCreateDTO
import android.content.Context
import com.example.dailyraily.R
import com.example.dailyraily.data.repository.TodoDAO
import com.example.dailyraily.ui.list.ItemData
import com.example.dailyraily.ui.list.Listable
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime.now
import java.util.UUID

class Todo(
    val uuid: UUID,
    val game: Game,
    var name: String,
    var goal: Int,
    var count: Int,
    var resetType: ResetType,
    var recentResetDate: LocalDate,
    var important: Boolean,
) : Listable {

    val done: Boolean
        get() = goal <= count


    val leftTime: Duration?
        get() = resetType.getNextResetDateTime(this).let { Duration.between(now(), (it)) }


    val priority: Int
        get() = resetType.getPriority(this)

    val resetPending: Boolean
        get() = resetType.isResetPending(this)


    init {
        game.register(this)
    }


    constructor(
        game: Game,
        name: String,
        goal: Int,
        resetType: ResetType,
        important: Boolean
    ) : this(
        UUID.randomUUID(),
        game,
        name,
        goal,
        0,
        resetType,
        game.adjustedDate,
        important
    )

    companion object {
        /** Loads only if the game to be connected is already loaded. */
        fun loadAllTodos(context: Context): List<Todo> {
            return TodoDAO(context).getAllTodos()
        }

        fun create(context: Context, dto: TodoCreateDTO) {
            val dao = TodoDAO(context)
            dao.insertTodo(Todo(dto.game, dto.name, dto.goal, dto.resetType, dto.important))
        }
    }

    private fun update(context: Context) {
        val dao = TodoDAO(context)
        dao.updateTodo(this)
    }

    fun reset(context: Context) {
        count = 0
        recentResetDate = game.adjustedDate

        update(context)
    }


    override fun toListItem(): ItemData {
        val timeString = leftTime?.let { "남은 시간: $it" } ?: R.string.no_reset_time.toString()
        return ItemData(
            "$name [ $count / $goal ] ",
            timeString,
            game.name,
            uuid.toString()
        )
    }


    override fun toString(): String {
        return "$name@$game#$uuid"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Todo

        return uuid == other.uuid
    }

    override fun hashCode(): Int {
        return uuid.hashCode()
    }


}