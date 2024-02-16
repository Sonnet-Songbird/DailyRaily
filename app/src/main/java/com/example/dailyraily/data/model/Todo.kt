package com.example.dailyraily.data.model

import java.time.LocalDate
import java.util.UUID

class Todo(private val game: Game, private var name: String, private val id: UUID) {
    private val gameName = game.name
    private var goal: Int = 0
    private var count: Int = 0
    private var recentResetDate: LocalDate = game.adjustedDate()
    private lateinit var resetType: ResetType
    private var important = false
    val done: Boolean
        get() = goal <= count
    val priority: Int
        get() = resetType.getPriority(this)
    val resetPending: Boolean
        get() = resetType.isResetPending(this)


    init {
        game.register(this, id)

    }

    constructor(
        game: Game,
        name: String,
        goal: Int,
        resetType: ResetType,
        important: Boolean
    ) : this(
        game,
        name,
        UUID.randomUUID()
    ) {
        this.goal = goal
        this.resetType = resetType
        this.important = important
    }

    fun reset() {
        count = 0
        recentResetDate = game.adjustedDate()
    }

    //Todo: Priority 산정 기준 반영.
    enum class ResetType {
        DAILY {
            override fun isResetPending(todo: Todo): Boolean {
                return todo.game.adjustedDate() != todo.recentResetDate
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
                val today = todo.game.adjustedDate()
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
                val today = todo.game.adjustedDate()
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
    }


    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Todo

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}