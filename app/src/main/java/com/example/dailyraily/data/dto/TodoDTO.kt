import com.example.dailyraily.data.model.Game
import com.example.dailyraily.data.model.ResetType
import com.example.dailyraily.data.model.Todo

data class TodoCreateDTO(
    val game: Game,
    val name: String,
    val goal: Int,
    val resetType: ResetType,
    val important: Boolean
)

data class TodoListWithPriority private constructor(var sortedTodoList: List<Todo>) {

    companion object {
        fun make(unsortedTodoList: List<Todo>): TodoListWithPriority {
            val sortedTodoList = unsortedTodoList.sortedByDescending { it.priority }
            return TodoListWithPriority(sortedTodoList)
        }
    }
}

