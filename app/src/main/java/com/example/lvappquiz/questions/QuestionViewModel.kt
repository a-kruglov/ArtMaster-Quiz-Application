package com.example.lvappquiz.questions

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lvappquiz.questions.QuestionData

class QuestionViewModel : ViewModel() {
    val question = MutableLiveData<QuestionData?>() //хранение текущего вопроса
    val onAnswerSelected: MutableLiveData<Int?> = MutableLiveData() //хранение индекса правильного ответа

    fun reset() { //подготавливает вьюмодель для нового вопроса
        question.value = null
        onAnswerSelected.value = null
    }
}