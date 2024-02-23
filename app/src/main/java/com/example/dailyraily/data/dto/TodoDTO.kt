import com.example.dailyraily.data.model.ResetType
import com.example.dailyraily.data.model.Todo
import java.util.UUID

data class TodoCreateDTO(
    val gameName: String,
    val name: String,
    val goal: Int,
    val resetType: ResetType,
    val important: Boolean
)

data class TodoUpdateDTO(
    val gameName: String,
    val uuid: UUID,
    val name: String,
    val goal: Int,
    val resetType: ResetType,
    val important: Boolean
)

data class TodoRemoveDTO(
    val gameName: String,
    val uuid: String
)


data class TodoListWithPriority private constructor(var sortedTodoList: List<Todo>) {

    companion object {
        fun make(unsortedTodoList: List<Todo>): TodoListWithPriority {
            val sortedTodoList = unsortedTodoList.sortedByDescending { it.priority }
            return TodoListWithPriority(sortedTodoList)
        }
    }
}

