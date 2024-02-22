package com.example.dailyraily.data.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.dailyraily.data.model.ResetType
import com.example.dailyraily.data.model.Todo
import com.example.dailyraily.data.model.TodoListManager
import java.util.UUID

class TodoDAO(context: Context) {
    private val dbHelper = DBHelper(context)

    fun insertTodo(todo: Todo) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.TODO_COLUMN_UUID, todo.uuid.toString())
            put(DBHelper.TODO_COLUMN_GAME, todo.game.name)
            put(DBHelper.TODO_COLUMN_NAME, todo.name)
            put(DBHelper.TODO_COLUMN_GOAL, todo.goal)
            put(DBHelper.TODO_COLUMN_COUNT, todo.count)
            put(DBHelper.TODO_COLUMN_RESETTYPE_NAME, todo.resetType.name)
            put(DBHelper.TODO_COLUMN_RECENTRESETDATE_TEXT, DBHelper.toText(todo.recentResetDate))
            put(DBHelper.TODO_COLUMN_IMPORTANT_BINARY, DBHelper.toBinary(todo.important))
        }

        db.insert(DBHelper.TABLE_TODOS, null, values)
        db.close()
    }

    fun getTodoById(uuid: UUID): Todo? {
        val db = dbHelper.readableDatabase

        val cursor: Cursor = db.query(
            DBHelper.TABLE_TODOS,
            null,
            "${DBHelper.TODO_COLUMN_UUID} = ?",
            arrayOf(uuid.toString()),
            null,
            null,
            null
        )

        var todo: Todo? = null

        if (cursor.moveToFirst()) {
            val gameIndex = cursor.getColumnIndex(DBHelper.TODO_COLUMN_GAME)
            val nameIndex = cursor.getColumnIndex(DBHelper.TODO_COLUMN_NAME)
            val goalIndex = cursor.getColumnIndex(DBHelper.TODO_COLUMN_GOAL)
            val countIndex = cursor.getColumnIndex(DBHelper.TODO_COLUMN_COUNT)
            val resetTypeIndex = cursor.getColumnIndex(DBHelper.TODO_COLUMN_RESETTYPE_NAME)
            val recentResetDateIndex =
                cursor.getColumnIndex(DBHelper.TODO_COLUMN_RECENTRESETDATE_TEXT)
            val importantIndex = cursor.getColumnIndex(DBHelper.TODO_COLUMN_IMPORTANT_BINARY)

            if (gameIndex != -1 && nameIndex != -1 && goalIndex != -1
                && countIndex != -1 && resetTypeIndex != -1 && importantIndex != -1
            ) {
                val name = cursor.getString(nameIndex)
                val gameName = cursor.getString(gameIndex)
                val goal = cursor.getInt(goalIndex)
                val count = cursor.getInt(countIndex)
                val resetTypeName = cursor.getString(resetTypeIndex)
                val recentResetDateTEXT = cursor.getString(recentResetDateIndex)
                val importantBinary = cursor.getInt(importantIndex)

                val game = TodoListManager.getGame(gameName) ?: throw IllegalStateException()
                val resetType = ResetType.of(resetTypeName)
                val recentResetDate = DBHelper.localDateFromText(recentResetDateTEXT)
                val important = DBHelper.booleanFromBinary(importantBinary)

                todo = Todo(
                    uuid,
                    game,
                    name,
                    goal,
                    count,
                    resetType,
                    recentResetDate,
                    important
                )
            } else {
                throw IllegalStateException()
            }
        }

        cursor.close()
        db.close()

        return todo
    }

    fun getAllTodos(): List<Todo> {
        val todos = mutableListOf<Todo>()
        val db = dbHelper.readableDatabase

        val cursor: Cursor = db.query(
            DBHelper.TABLE_TODOS,
            null,
            null,
            null,
            null,
            null,
            null
        )
        val uuidIndex = cursor.getColumnIndex(DBHelper.TODO_COLUMN_UUID)
        val nameIndex = cursor.getColumnIndex(DBHelper.TODO_COLUMN_NAME)
        val gameIndex = cursor.getColumnIndex(DBHelper.TODO_COLUMN_GAME)
        val goalIndex = cursor.getColumnIndex(DBHelper.TODO_COLUMN_GOAL)
        val countIndex = cursor.getColumnIndex(DBHelper.TODO_COLUMN_COUNT)
        val recentResetDateIndex =
            cursor.getColumnIndex(DBHelper.TODO_COLUMN_RECENTRESETDATE_TEXT)
        val resetTypeIndex = cursor.getColumnIndex(DBHelper.TODO_COLUMN_RESETTYPE_NAME)

        val importantIndex = cursor.getColumnIndex(DBHelper.TODO_COLUMN_IMPORTANT_BINARY)
        if (nameIndex != -1 && gameIndex != -1 && goalIndex != -1
            && countIndex != -1 && resetTypeIndex != -1 && importantIndex != -1
        ) {
            while (cursor.moveToNext()) {
                val uuidString = cursor.getString(uuidIndex)
                val name = cursor.getString(nameIndex)
                val gameName = cursor.getString(gameIndex)
                val goal = cursor.getInt(goalIndex)
                val count = cursor.getInt(countIndex)
                val resetTypeName = cursor.getString(resetTypeIndex)
                val recentResetDateTEXT = cursor.getString(recentResetDateIndex)
                val importantBinary = cursor.getInt(importantIndex)

                val game = TodoListManager.getGame(gameName) ?: continue
                val resetType = ResetType.of(resetTypeName)
                val recentResetDate = DBHelper.localDateFromText(recentResetDateTEXT)
                val important = DBHelper.booleanFromBinary(importantBinary)

                val todo = Todo(
                    UUID.fromString(uuidString),
                    game,
                    name,
                    goal,
                    count,
                    resetType,
                    recentResetDate,
                    important
                )

                todos.add(todo)
            }
        } else {
            throw IllegalStateException()
        }
        cursor.close()
        db.close()

        return todos
    }


    fun updateTodo(todo: Todo) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.TODO_COLUMN_UUID, todo.uuid.toString())
            put(DBHelper.TODO_COLUMN_GAME, todo.game.name)
            put(DBHelper.TODO_COLUMN_NAME, todo.name)
            put(DBHelper.TODO_COLUMN_GOAL, todo.goal)
            put(DBHelper.TODO_COLUMN_COUNT, todo.count)
            put(DBHelper.TODO_COLUMN_RESETTYPE_NAME, todo.resetType.name)
            put(DBHelper.TODO_COLUMN_RECENTRESETDATE_TEXT, DBHelper.toText(todo.recentResetDate))
            put(DBHelper.TODO_COLUMN_IMPORTANT_BINARY, DBHelper.toBinary(todo.important))
        }

        db.update(
            DBHelper.TABLE_TODOS,
            values,
            "${DBHelper.TODO_COLUMN_UUID} = ?",
            arrayOf(todo.uuid.toString())
        )
        db.close()
    }

    fun deleteTodoById(uuid: UUID) {
        val db = dbHelper.writableDatabase
        db.delete(
            DBHelper.TABLE_TODOS,
            "${DBHelper.TODO_COLUMN_UUID} = ?",
            arrayOf(uuid.toString())
        )
        db.close()
    }
}
