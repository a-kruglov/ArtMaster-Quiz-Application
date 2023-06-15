package com.example.lvappquiz.lives

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LivesViewModel : ViewModel() {
    val lives = MutableLiveData(3)
    fun reset() {
        lives.value = 3
    }
}
