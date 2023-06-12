package com.example.lvappquiz.user_progress

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProgressViewModel : ViewModel() {

    val progress = MutableLiveData(0)

    fun reset() {
        progress.value = 0
    }
}
