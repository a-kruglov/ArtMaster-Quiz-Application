package com.example.lvappquiz.questions.fragments.specific
import androidx.fragment.app.Fragment
import com.example.lvappquiz.questions.QuestionType
import com.example.lvappquiz.questions.fragments.AbstractQuestionsFragment
import com.example.lvappquiz.questions.fragments.TextQuestionTextAnswersFragment

class GuessBirthYearOfAuthorQuestionFragment : AbstractQuestionsFragment() {

    override val questionType: QuestionType = QuestionType.GuessBirthYearOfAuthor

    override fun getQuestionFragment(): Fragment = TextQuestionTextAnswersFragment()
}