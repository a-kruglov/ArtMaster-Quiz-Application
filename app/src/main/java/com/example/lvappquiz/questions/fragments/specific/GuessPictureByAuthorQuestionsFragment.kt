package com.example.lvappquiz.questions.fragments.specific
import androidx.fragment.app.Fragment
import com.example.lvappquiz.questions.QuestionType
import com.example.lvappquiz.questions.fragments.AbstractQuestionsFragment
import com.example.lvappquiz.questions.fragments.TextQuestionImageAnswersFragment

class GuessPictureByAuthorQuestionsFragment : AbstractQuestionsFragment() {

    override val questionType: QuestionType = QuestionType.GuessPictureByAuthor

    override fun getQuestionFragment(): Fragment = TextQuestionImageAnswersFragment()
}