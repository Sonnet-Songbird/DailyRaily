package com.example.dailyraily.ui.list

data class ListData(val data: Array<ListItem>) {
    val size: Int
        get() = data.size

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ListData

        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }

    operator fun get(position: Int): ListItem {
        return data[position]
    }
}

data class ListItem(var textArray: Array<String>, var boolean: Boolean ) {
    val text: CharSequence
        get() = arrayToText(textArray)

    private fun arrayToText(array: Array<String>): CharSequence {

        val builder = StringBuilder()
        for (line: String in textArray) {
            builder.append(line).append("\n")
        }
        return builder.toString()
    }
}
