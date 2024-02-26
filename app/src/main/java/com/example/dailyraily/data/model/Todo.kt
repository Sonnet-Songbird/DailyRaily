package com.example.dailyraily.data.model

import TodoCreateDTO
import TodoUpdateDTO
import android.content.Context
import android.util.Log
import com.example.dailyraily.data.repository.TodoDAO
import com.example.dailyraily.data.service.TodoListManager
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


    private val leftTime: Duration
        get() {
            val nextResetDateTime = resetType.getNextResetDateTime(this)
            return nextResetDateTime?.let { Duration.between(now(), it) } ?: Duration.ZERO
        }


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

    fun update(context: Context, dto: TodoUpdateDTO) {
        this.name = dto.name
        this.goal = dto.goal
        this.resetType = dto.resetType
        this.important = dto.important

        updateDB(context)
    }

    fun reset(context: Context) {
        count = 0
        recentResetDate = game.adjustedDate
        updateDB(context)
    }

    //TODO 이벤트 리스너 문제 해결되면 삭제
    fun discount(context: Context) {
        if (count != 0) {
            count--
            updateDB(context)
        }
    }

    fun count(context: Context) {
        if (!done) {
            count++
            updateDB(context)
        }
    }


    companion object {
        /** Loads only if the game to be connected is already loaded. */
        fun loadAllTodos(context: Context): List<Todo> {
            return TodoDAO(context).getAllTodos()
        }

        fun create(context: Context, dto: TodoCreateDTO) {
            val dao = TodoDAO(context)
            val game = TodoListManager.getGame(dto.gameName)
            val goal = if (dto.goal == 0) 1 else dto.goal
            dao.insertTodo(Todo(game, dto.name, goal, dto.resetType, dto.important))
        }
    }

    fun remove(context: Context) {
        val dao = TodoDAO(context)
        game.deregister(uuid)

        dao.deleteTodo(uuid)
    }

    private fun updateDB(context: Context) {
        val dao = TodoDAO(context)
        dao.updateTodo(this)
    }


    override fun toListItem(): ItemDTO {
        val timeString = leftTimeString()
        val columnTwo: String
        if (resetType != ResetType.NORMAL) {
            columnTwo = timeString
        } else columnTwo = "초기화 없음"
        return ItemDTO(
            "$name [${resetType.korName()}]",
            columnTwo,
            game.name,
            uuid.toString(),
            ItemDTO.ItemType.TODO,
            this.done,
            "$count|$goal"
        )
    }

    override fun getId(): String {
        return "${game.name}|${uuid}"
    }

    fun leftTimeString(): String {
        val duration = leftTime ?: throw IllegalStateException()
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