package com.example.lvappquiz.questions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lvappquiz.questions.QuestionData

class QuestionViewModel : ViewModel() {
    val question = MutableLiveData<QuestionData?>()
    val onAnswerSelected: MutableLiveData<Int?> = MutableLiveData()
    fun reset() {
        question.value = null
        onAnswerSelected.value = null
    }
}