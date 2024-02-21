package com.example.dailyraily.data.model

import android.content.Context
import com.example.dailyraily.data.repository.TodoDAO
import java.time.LocalDate
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
) {

    val done: Boolean
        get() = goal <= count

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


    //Todo: Priority 산정 기준 반영.
    enum class ResetType {
        DAILY {
            override fun isResetPending(todo: Todo): Boolean {
                return todo.game.adjustedDate != todo.recentResetDate
            }

            override fun getPriority(todo: Todo): Int {
                return if (todo.done) {
                    0
                } else {
                    if (todo.important)
                        101
                    else {
                        100
                    }
                }
            }

        },
        WEEKLY {
            override fun isResetPending(todo: Todo): Boolean {
                val today = todo.game.adjustedDate
                val dateDifference =
                    (today.dayOfWeek.value - todo.game.resetDOW.value + 7) % 7
                val resetDate = today.minusDays(dateDifference.toLong());

                return resetDate > todo.recentResetDate
            }

            override fun getPriority(todo: Todo): Int {
                return if (todo.done) {
                    0
                } else {
                    if (todo.important)
                        101
                    else {
                        100
                    }
                }
            }

        },
        MONTHLY {
            override fun isResetPending(todo: Todo): Boolean {
                val today = todo.game.adjustedDate
                val resetDay = todo.game.resetDay
                var resetDate = today.withDayOfMonth(resetDay)
                if (today.dayOfMonth > resetDay)
                    resetDate = resetDate.minusMonths(1)
                return resetDate > todo.recentResetDate
            }

            override fun getPriority(todo: Todo): Int {
                return if (todo.done) {
                    0
                } else {
                    if (todo.important)
                        101
                    else {
                        100
                    }
                }
            }

        },
        NORMAL {
            override fun isResetPending(todo: Todo): Boolean {
                return false
            }

            override fun getPriority(todo: Todo): Int {
                return if (todo.done) {
                    0
                } else {
                    if (todo.important)
                        101
                    else {
                        100
                    }
                }
            }
        };

        abstract fun isResetPending(todo: Todo): Boolean
        abstract fun getPriority(todo: Todo): Int

        companion object {
            fun of(ordinal: Int): ResetType {
                return entries[ordinal]
            }
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