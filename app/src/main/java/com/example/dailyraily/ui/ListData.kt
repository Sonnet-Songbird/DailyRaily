package com.example.dailyraily.ui

data class ListData(val data: Array<String>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ListData

        return data.contentEquals(other.data)
    }

    override fun hashCode(): Int {
        return data.contentHashCode()
    }
}
