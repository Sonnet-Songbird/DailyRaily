//todo 전체 재작성 필요
package com.example.dailyraily.ui.list

import TodoRemoveDTO
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyraily.R
import com.example.dailyraily.data.dto.GameRemoveDTO
import com.example.dailyraily.data.service.TodoListManager
import java.time.DayOfWeek

class ListAdapter(private var data: List<Listable>, val context: Context) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {
    private val alertDialog: ItemDeleteAlertDialog = ItemDeleteAlertDialog(context)
    private val handler: Handler = Handler(Looper.getMainLooper())

    init {
        handler.postDelayed({
            notifyDataSetChanged()
            handler.postDelayed(this::updateDataPeriodically, 1000)
        }, 1000)
    }

    private fun updateDataPeriodically() {
        handler.postDelayed(this::updateDataPeriodically, 1000)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutRes = R.layout.item_layout
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {
        private val textView1: TextView = itemView.findViewById(R.id.textView1)
        private val textView2: TextView = itemView.findViewById(R.id.textView2)
        private val textView3: TextView = itemView.findViewById(R.id.textView3)
        private val stroke: View = itemView.findViewById(R.id.strokeLine)
        private val chkbox: CheckBox = itemView.findViewById(R.id.checkBox)
        private val countTextView: TextView = itemView.findViewById(R.id.countTextView)


        fun bind(item: Listable) {
            val dto = item.toListItem()
            textView1.text = dto.columnOne
            textView2.text = dto.columnTwo
            textView3.text = dto.columnThree
            if (dto.type == ItemDTO.ItemType.TODO) {
                val count: Int = dto.variable.split("|")[0].toInt()
                val goal: Int = dto.variable.split("|")[1].toInt()
                if (goal == 1) {
                    chkbox.visibility = View.VISIBLE
                    countTextView.visibility = View.GONE
                    chkbox.isChecked = (count == goal)
                } else {
                    countTextView.visibility = View.VISIBLE
                    chkbox.visibility = View.GONE
                    countTextView.text = "[$count/$goal]"
                }
            }
            if (dto.isDone) {
                stroke.visibility = View.VISIBLE
            } else {
                stroke.visibility = View.GONE
            }
        }

        init {

            //카운트
            var lastClickTime: Long = 0
            val DOUBLE_CLICK_TIME_DELTA: Long = 300 // 더블클릭 간격

            itemView.setOnClickListener {
                val item = data[adapterPosition]
                val clickTime = System.currentTimeMillis()

                if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                    val id = item.getId().split("|")
                    when (item.toListItem().type) {
                        ItemDTO.ItemType.GAME -> {
                            alertDialog.showConfirmationDialog(
                                "삭제",
                                "이 게임을 삭제하시겠습니까?",
                                "예",
                                "아니오",
                                {
                                    TodoListManager.removeGame(
                                        alertDialog.context,
                                        GameRemoveDTO(item.getId())
                                    )
                                    removeItem(item.getId(), adapterPosition)

                                },
                                {

                                }
                            )
                        }

                        ItemDTO.ItemType.TODO -> {
                            TodoListManager.discountTodo(alertDialog.context, id[0], id[1])
                            alertDialog.showConfirmationDialog(
                                "삭제",
                                "이 할일을 삭제하시겠습니까?",
                                "예",
                                "아니오",
                                {
                                    val id = item.getId().split("|")
                                    TodoListManager.removeTodo(
                                        alertDialog.context,
                                        TodoRemoveDTO(id[0], id[1])
                                    )
                                    removeItem(item.getId(), adapterPosition)


                                },
                                {

                                }
                            )
                        }
                    }
                } else {
                    // 단일 클릭 처리
                    when (item.toListItem().type) {
                        ItemDTO.ItemType.GAME -> {
                            // 게임에 대한 단일 클릭 처리
                        }

                        ItemDTO.ItemType.TODO -> {
                            val id = item.getId().split("|")
                            Log.d("test", "touch")
                            TodoListManager.countTodo(alertDialog.context, id[0], id[1])
                            notifyItemChanged(adapterPosition)
                        }
                    }
                }

                lastClickTime = clickTime
            }


            //네비게이션바 등록 / 초기화
            itemView.setOnLongClickListener {
                val item = data[adapterPosition]
                when (item.toListItem().type) {
                    ItemDTO.ItemType.GAME -> {
//todo 추후 필터기능 구현
                    }

                    ItemDTO.ItemType.TODO -> {
                        val id = item.getId().split("|")
                        TodoListManager.resetTodo(context, id[0], id[1])
                    }
                }
                notifyItemChanged(adapterPosition)

                true
            }
        }
    }

    private fun removeItem(id: String, index: Int) {
        data = data.filterIndexed { _, item -> item.getId() != id }
        notifyItemRemoved(index)
    }
}

interface Listable {
    fun toListItem(): ItemDTO
    fun getId(): String
}

fun dowToString(dow: DayOfWeek): String {
    return when (dow) {
        DayOfWeek.MONDAY -> "월요일"
        DayOfWeek.TUESDAY -> "화요일"
        DayOfWeek.WEDNESDAY -> "수요일"
        DayOfWeek.THURSDAY -> "목요일"
        DayOfWeek.FRIDAY -> "금요일"
        DayOfWeek.SATURDAY -> "토요일"
        DayOfWeek.SUNDAY -> "일요일"
    }
}

data class ItemDTO(
    val columnOne: String,
    val columnTwo: String,
    val columnThree: String,
    val id: String,
    val type: ItemType,
    var isDone: Boolean = false,
    val variable: String = "",
    var isSelected: Boolean = false
) {
    enum class ItemType {
        GAME,
        TODO
    }
}

data class HeaderTodoDTO(
    val columnOne: String,
    val columnTwo: String,
    val columnThree: String,
    val id: String,
)

inline fun View.setOnDoubleClickListener(crossinline onDoubleClick: () -> Unit) {
    var lastClickTime: Long = 0
    setOnClickListener {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            onDoubleClick()
        }
        lastClickTime = clickTime
    }
}

const val DOUBLE_CLICK_TIME_DELTA: Long = 300 // 더블클릭 간격

class ItemDeleteAlertDialog(val context: Context) {

    fun showConfirmationDialog(
        title: String,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String,
        onPositiveClick: () -> Unit,
        onNegativeClick: () -> Unit
    ) {
        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)

        alertDialogBuilder.setPositiveButton(positiveButtonText) { _, _ ->
            onPositiveClick.invoke()
        }

        alertDialogBuilder.setNegativeButton(negativeButtonText) { _, _ ->
            onNegativeClick.invoke()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}


