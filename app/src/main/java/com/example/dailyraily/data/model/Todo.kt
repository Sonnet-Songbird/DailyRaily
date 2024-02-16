package com.example.dailyraily.data.model

import java.time.LocalDate
import java.util.UUID

class Todo(private val game: Game, private val name: String, private val id: UUID) {
    private var gameName = game.name
    private var goal: Int = 0
    private var count: Int = 0
    private var recentReset: LocalDate = game.adjustedDate()
    private lateinit var resetType: ResetType

    init {
        game.register(this, id)

    }

    constructor(game: Game, name: String, goal: Int, resetType: ResetType) : this(
        game,
        name,
        UUID.randomUUID()
    ) {
        this.goal = goal
        this.resetType = resetType
    }

    fun reset() {
        count = 0
        recentReset = game.adjustedDate()
    }

    fun resetAll() {}
    enum class ResetType {
        DAILY,
        WEEKLY,
        MONTHLY,
        NORMAL
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