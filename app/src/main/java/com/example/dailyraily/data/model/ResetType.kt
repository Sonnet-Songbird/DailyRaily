package com.example.dailyraily.data.model

import java.time.LocalDateTime

//Todo: Priority 산정 기준 반영.
enum class ResetType {
    DAILY {
        override fun cycle(localDateTime: LocalDateTime, long: Long): LocalDateTime {
            return localDateTime.plusDays(long)
        }

        override fun getNextResetDateTime(todo: Todo): LocalDateTime? {
            val todayReset = LocalDateTime.now().withHour(todo.game.resetHour).withMinute(0)
            return if (LocalDateTime.now().isAfter(todayReset)) {
                cycle(todayReset, 1)
            } else {
                todayReset
            }
        }

        override fun getPriority(todo: Todo): Int {
            return if (todo.done) {
                0
            } else {
                if (todo.important)
                    103
                else {
                    102
                }
            }
        }

        override fun korName(): String {
            return "일일"
        }

    },
    WEEKLY {
        override fun cycle(localDateTime: LocalDateTime, long: Long): LocalDateTime {
            return localDateTime.plusWeeks(long)
        }

        override fun getNextResetDateTime(todo: Todo): LocalDateTime? {
            val today = todo.game.adjustedDate
            val dateDifference = (todo.game.resetDOW.value - today.dayOfWeek.value + 7) % 7
            val nextResetDate = today.plusDays((dateDifference + 7).toLong())
            return nextResetDate.atTime(todo.game.resetHour, 0)
        }


        override fun getPriority(todo: Todo): Int {
            return if (todo.done) {
                0
            } else {
                if (todo.important)
                    102
                else {
                    101
                }
            }
        }

        override fun korName(): String {
            return "주간"
        }

    },
    MONTHLY {
        override fun cycle(localDateTime: LocalDateTime, long: Long): LocalDateTime {
            return localDateTime.plusMonths(long)
        }

        override fun getNextResetDateTime(todo: Todo): LocalDateTime? {
            val resetDay = todo.game.resetDay
            val today = todo.game.adjustedDate
            val resetDateTime = today.withDayOfMonth(resetDay).atTime(todo.game.resetHour, 0)

            if (todo.game.adjustedDate.dayOfMonth > todo.game.resetDay) {
                return resetDateTime.plusMonths(1)
            } else {
                return resetDateTime
            }
        }

        override fun korName(): String {
            return "월간"
        }

        override fun getPriority(todo: Todo): Int {
            return if (todo.done) {
                0
            } else {
                if (todo.important)
                    105
                else {
                    104
                }
            }
        }

    },
    NORMAL {
        override fun cycle(localDateTime: LocalDateTime, long: Long): LocalDateTime {
            return localDateTime
        }

        override fun getNextResetDateTime(todo: Todo): LocalDateTime? {
            return null
        }

        override fun korName(): String {
            return "수동"
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
    fun isResetPending(todo: Todo): Boolean {
        val resetDateTime = getNextResetDateTime(todo)

        return resetDateTime?.let {
            val resetDate = cycle(it, -1).toLocalDate()
            resetDate.isAfter(todo.recentResetDate)
        } ?: false
    }

    abstract fun cycle(localDateTime: LocalDateTime, long: Long): LocalDateTime

    abstract fun getPriority(todo: Todo): Int
    abstract fun korName(): String

    companion object {
        fun of(name: String): ResetType {
            return valueOf(name)
        }
    }
}