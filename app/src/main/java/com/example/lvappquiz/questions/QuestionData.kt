package com.example.lvappquiz.questions
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize //данные о вопросе
data class QuestionData(
    val type: QuestionType,
    val image: String?,
    val questionText: String,
    val answers: List<String>,
    val correctAnswerIndex: Int,
    val fact: String?
): Parcelable