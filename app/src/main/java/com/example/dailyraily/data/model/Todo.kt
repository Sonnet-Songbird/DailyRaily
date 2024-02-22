package com.example.dailyraily.data.model

import TodoCreateDTO
import android.content.Context
import com.example.dailyraily.R
import com.example.dailyraily.data.repository.TodoDAO
import com.example.dailyraily.ui.list.ItemDTO
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


    override fun toListItem(): ItemDTO {
        val timeString = leftTime?.let { formatTimeString(it) } ?: R.string.no_reset_time.toString()
        return ItemDTO(
            "$name [ $count / $goal ] ",
            timeString,
            game.name,
            uuid.toString()
        )
    }

    private fun formatTimeString(duration: Duration): String {
        val days = duration.toDays()
        val hours = duration.toHours() % 24
        val minutes = duration.toMinutes() % 60
        val seconds = duration.seconds % 60

        return when {
            days > 0 -> "남은 시간: ${days}일 ${hours}시간 ${minutes}분"
            hours > 0 -> "남은 시간: ${hours}시간 ${minutes}분"
            else -> "남은 시간: ${minutes}분 ${seconds}초"
        }
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