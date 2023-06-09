package com.example.lvappquiz.quiz
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lvappquiz.questions.QuestionData
import com.example.lvappquiz.questions.QuestionType

class QuizViewModel : ViewModel() {

    val questionType = MutableLiveData<QuestionType?>()

    fun reset() {
        questionType.value = null
    }
}