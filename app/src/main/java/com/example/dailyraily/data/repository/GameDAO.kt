package com.example.dailyraily.data.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.util.Log
import com.example.dailyraily.data.model.Game
import java.time.DayOfWeek

class GameDAO(context: Context) {
    private val dbHelper = DBHelper(context)

    fun insertGame(game: Game) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.GAME_COLUMN_NAME, game.name)
            put(DBHelper.GAME_COLUMN_RESETDAY, game.resetDay)
            put(DBHelper.GAME_COLUMN_RESETDOW_TEXT, game.resetDOW.name)
            put(DBHelper.GAME_COLUMN_RESETHOUR, game.resetHour)

            Log.d("test", "insert ${game.resetDOW.name}")
        }

        db.insert(DBHelper.TABLE_GAMES, null, values)
        db.close()
    }

    fun getGame(name: String): Game? {
        val db = dbHelper.readableDatabase

        val cursor: Cursor = db.query(
            DBHelper.TABLE_GAMES,
            null,
            "${DBHelper.GAME_COLUMN_NAME} = ?",
            arrayOf(name),
            null,
            null,
            null
        )

        var game: Game? = null

        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(DBHelper.GAME_COLUMN_NAME)
            val resetDayIndex = cursor.getColumnIndex(DBHelper.GAME_COLUMN_RESETDAY)
            val resetDOWIndex = cursor.getColumnIndex(DBHelper.GAME_COLUMN_RESETDOW_TEXT)
            val resetHourIndex = cursor.getColumnIndex(DBHelper.GAME_COLUMN_RESETHOUR)

            if (nameIndex != -1 && resetHourIndex != -1 && resetDOWIndex != -1 && resetDayIndex != -1) {
                val gameName = cursor.getString(nameIndex)
                val resetDay = cursor.getInt(resetDayIndex)
                val resetDOW = cursor.getString(resetDOWIndex)
                val resetHour = cursor.getInt(resetHourIndex)
                try {
                    game = Game(gameName, resetDay, DayOfWeek.valueOf(resetDOW), resetHour)

                } catch (e: Exception) {
                    deleteGame(gameName)
                }
            } else {
                throw IllegalStateException()
            }
        }

        cursor.close()
        db.close()

        return game
    }

    fun getAllGames(): List<Game> {
        val games = mutableListOf<Game>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(DBHelper.TABLE_GAMES, null, null, null, null, null, null)

        while (cursor.moveToNext()) {
            val nameIndex = cursor.getColumnIndex(DBHelper.GAME_COLUMN_NAME)
            val resetDayIndex = cursor.getColumnIndex(DBHelper.GAME_COLUMN_RESETDAY)
            val resetDOWIndex = cursor.getColumnIndex(DBHelper.GAME_COLUMN_RESETDOW_TEXT)
            val resetHourIndex = cursor.getColumnIndex(DBHelper.GAME_COLUMN_RESETHOUR)

            if (nameIndex != -1 && resetHourIndex != -1 && resetDOWIndex != -1 && resetDayIndex != -1) {
                val gameName = cursor.getString(nameIndex)
                val resetDay = cursor.getInt(resetDayIndex)
                val resetDOW = cursor.getString(resetDOWIndex)
                val resetHour = cursor.getInt(resetHourIndex)
                try {
                    games.add(Game(gameName, resetDay, DayOfWeek.valueOf(resetDOW), resetHour))
                } catch (e: Exception) {
                    Log.d("test", "getAll ${resetDOW}")

                    deleteGame(gameName)
                }
            } else {
                throw IllegalStateException()
            }
        }

        cursor.close()
        db.close()

        return games
    }

    fun updateGame(game: Game) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.GAME_COLUMN_NAME, game.name)
            put(DBHelper.GAME_COLUMN_RESETDAY, game.resetDay)
            put(DBHelper.GAME_COLUMN_RESETDOW_TEXT, game.resetDOW.name)
            put(DBHelper.GAME_COLUMN_RESETHOUR, game.resetHour)
        }
        db.update(
            DBHelper.TABLE_GAMES,
            values,
            "${DBHelper.GAME_COLUMN_NAME} = ?",
            arrayOf(game.name)
        )
        db.close()
    }

    fun deleteGame(name: String) {
        val db = dbHelper.writableDatabase
        db.delete(DBHelper.TABLE_GAMES, "${DBHelper.GAME_COLUMN_NAME} = ?", arrayOf(name))
        db.close()
    }
}
