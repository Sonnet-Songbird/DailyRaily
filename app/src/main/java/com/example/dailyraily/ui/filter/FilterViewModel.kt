package com.example.dailyraily.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Filter Fragment"
    }
    val text: LiveData<String> = _text
}