package com.example.lvappquiz.questions.fragments.specific
import androidx.fragment.app.Fragment
import com.example.lvappquiz.questions.QuestionType
import com.example.lvappquiz.questions.QuestionViewType
import com.example.lvappquiz.questions.fragments.ImageQuestionTextAnswersFragment
import com.example.lvappquiz.questions.fragments.AbstractQuestionsFragment
import com.example.lvappquiz.questions.fragments.TextQuestionImageAnswersFragment
import com.example.lvappquiz.questions.fragments.TextQuestionTextAnswersFragment

class RandomQuestionsFragment : AbstractQuestionsFragment() {

    override val questionType: QuestionType = QuestionType.Random // Установка типа вопроса для этого фрагмента

    private val questionTypeMap: Map<QuestionType, QuestionViewType> = mapOf(
        QuestionType.GuessBirthYearOfAuthor to QuestionViewType.TextQuestionTextAnswers,
        QuestionType.GuessAuthorByPicture to QuestionViewType.ImageQuestionTextAnswers,
        QuestionType.GuessYearByPicture to QuestionViewType.ImageQuestionTextAnswers,
        QuestionType.GuessPictureByAuthor to QuestionViewType.TextQuestionImageAnswers,
    )

    override fun getQuestionFragment(): Fragment {
        return when (getQuestionViewType()) {
            QuestionViewType.ImageQuestionTextAnswers -> ImageQuestionTextAnswersFragment()
            QuestionViewType.TextQuestionImageAnswers -> TextQuestionImageAnswersFragment()
            QuestionViewType.TextQuestionTextAnswers -> TextQuestionTextAnswersFragment()
            else -> throw IllegalArgumentException("Invalid question view type")
        }
    }

    private fun getQuestionViewType(): QuestionViewType {
        val questionType = questionViewModel.question.value?.type // Получение типа вопроса из QuestionViewModel
        return questionTypeMap[questionType] ?: throw IllegalArgumentException("Invalid question type") // Получение соответствующего типа представления вопроса из карты questionTypeMap
    }
}

