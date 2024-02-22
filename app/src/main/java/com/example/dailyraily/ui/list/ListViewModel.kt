package com.example.dailyraily.ui.list

import androidx.lifecycle.ViewModel

class ListViewModel : ViewModel() {
    val data: MutableList<Listable> = mutableListOf()
}