import com.example.dailyraily.data.model.Game
import com.example.dailyraily.data.model.Todo

data class TodoCreateDTO(
    val game: Game,
    val name: String,
    val goal: Int,
    val resetType: Todo.ResetType,
    val important: Boolean
)
