package com.example.dailyraily.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.UUID

@Entity(
    tableName = "todos",
    foreignKeys = [
        ForeignKey(
            entity = Game::class,
            parentColumns = ["name"],
            childColumns = ["gameName"]
        )
    ]
)
class Todo(
    private val id: UUID,
    val game: Game,
    private var name: String,
    private var recentResetDate: LocalDate
) {
    private val gameName = game.name
    private var goal: Int = 0
    private var count: Int = 0
    private lateinit var resetType: ResetType
    private var important = false
    private val _todoLiveData = MutableLiveData<Todo>()
    val todoLiveData: LiveData<Todo> get() = _todoLiveData

    @get:Ignore
    val done: Boolean
        get() = goal <= count

    @get:Ignore
    val priority: Int
        get() = resetType.getPriority(this)

    @get:Ignore
    val resetPending: Boolean
        get() = resetType.isResetPending(this)


    init {
        game.register(this)
        _todoLiveData.value = this
    }
    fun updateTodo() {
        _todoLiveData.value = this
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
        game.adjustedDate
    ) {
        this.goal = goal
        this.resetType = resetType
        this.important = important
    }

    fun reset() {
        count = 0
        recentResetDate = game.adjustedDate
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