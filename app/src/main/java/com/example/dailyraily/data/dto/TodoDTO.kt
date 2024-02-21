import com.example.dailyraily.data.model.Game
import com.example.dailyraily.data.model.ResetType

data class TodoCreateDTO(
    val game: Game,
    val name: String,
    val goal: Int,
    val resetType: ResetType,
    val important: Boolean
)
