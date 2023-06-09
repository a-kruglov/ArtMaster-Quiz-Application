package com.example.lvappquiz.lives

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LivesViewModel : ViewModel() {

    // MutableLiveData для хранения количества жизней
    val lives = MutableLiveData(3)

    // Метод для сброса количества жизней в исходное значение
    fun reset() {
        lives.value = 3
    }
}
