package com.example.dailyraily.data.model

import java.time.LocalDateTime

//Todo: Priority 산정 기준 반영.
enum class ResetType {
    DAILY {
        override fun getNextResetDateTime(todo: Todo): LocalDateTime? {
            TODO("Not yet implemented")
        }

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
        override fun getNextResetDateTime(todo: Todo): LocalDateTime? {
            TODO("Not yet implemented")
        }

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
        override fun getNextResetDateTime(todo: Todo): LocalDateTime? {
            TODO("Not yet implemented")
        }

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
        override fun getNextResetDateTime(todo: Todo): LocalDateTime? {
            return null
        }

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

    abstract fun getNextResetDateTime(todo: Todo): LocalDateTime?
    abstract fun isResetPending(todo: Todo): Boolean
    abstract fun getPriority(todo: Todo): Int

    companion object {
        fun of(ordinal: Int): ResetType {
            return entries[ordinal]
        }
    }
}