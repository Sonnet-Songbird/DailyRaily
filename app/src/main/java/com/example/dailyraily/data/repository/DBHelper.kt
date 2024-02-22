package com.example.dailyraily.data.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.LocalDate

class DBHelper(val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(createGameTableQuery)
        db.execSQL(createTodoTableQuery)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    companion object {
        const val DATABASE_NAME = "data.db"
        const val DATABASE_VERSION = 1

        const val TABLE_GAMES = "games"
        const val GAME_COLUMN_NAME = "name"
        const val GAME_COLUMN_RESETDAY = "reset_day"
        const val GAME_COLUMN_RESETDOW_ORDINAL = "reset_dow_ordinal"
        const val GAME_COLUMN_RESETHOUR = "reset_hour"

        private val createGameTableQuery = """
            CREATE TABLE $TABLE_GAMES (
                $GAME_COLUMN_NAME TEXT PRIMARY KEY,
                $GAME_COLUMN_RESETDAY INTEGER,
                $GAME_COLUMN_RESETDOW_ORDINAL INTEGER,
                $GAME_COLUMN_RESETHOUR INTEGER
            )
        """.trimIndent()


        const val TABLE_TODOS = "todos"
        const val TODO_COLUMN_UUID = "uuid"
        const val TODO_COLUMN_GAME = "game"
        const val TODO_COLUMN_NAME = "name"
        const val TODO_COLUMN_GOAL = "goal"
        const val TODO_COLUMN_COUNT = "count"
        const val TODO_COLUMN_RESETTYPE_NAME = "reset_type_name"
        const val TODO_COLUMN_RECENTRESETDATE_TEXT = "recent_reset_date_text"
        const val TODO_COLUMN_IMPORTANT_BINARY = "important_binary"


        private val createTodoTableQuery = """
            CREATE TABLE $TABLE_TODOS (
                $TODO_COLUMN_UUID BLOB PRIMARY KEY,
                $TODO_COLUMN_GAME TEXT REFERENCES $TABLE_GAMES($GAME_COLUMN_NAME),
                $TODO_COLUMN_NAME TEXT,
                $TODO_COLUMN_GOAL INTEGER,
                $TODO_COLUMN_COUNT INTEGER,
                $TODO_COLUMN_RESETTYPE_NAME INTEGER,
                $TODO_COLUMN_RECENTRESETDATE_TEXT TEXT,
                $TODO_COLUMN_IMPORTANT_BINARY INTEGER
            )
        """.trimIndent()

        fun toText(localDate: LocalDate): String {
            return localDate.toString()
        }

        fun localDateFromText(text: String): LocalDate {
            return LocalDate.parse(text)
        }

        fun toBinary(boolean: Boolean): Int {
            return if (boolean) {
                1
            } else {
                0
            }
        }

        fun booleanFromBinary(integer: Int): Boolean {
            return when (integer) {
                1 -> true
                0 -> false
                else -> throw IllegalArgumentException("booleanFromBinary can't parse $integer")
            }
        }
    }


}
