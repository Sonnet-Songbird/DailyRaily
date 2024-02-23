package com.example.dailyraily.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dailyraily.R
import java.time.DayOfWeek

class ListAdapter(private val data: List<Listable>) :
    RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutRes = R.layout.item_layout
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])

    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView1: TextView = itemView.findViewById(R.id.textView1)
        private val textView2: TextView = itemView.findViewById(R.id.textView2)
        private val textView3: TextView = itemView.findViewById(R.id.textView3)

        fun bind(item: Listable) {
            val dto = item.toListItem()
            textView1.text = dto.columnOne
            textView2.text = dto.columnOne
            textView3.text = dto.columnOne
        }
    }
}

interface Listable {
    fun toListItem(): ItemDTO
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
    val type: ItemType
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