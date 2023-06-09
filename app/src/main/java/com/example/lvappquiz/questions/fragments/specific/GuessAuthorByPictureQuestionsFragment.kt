package com.example.lvappquiz.questions.fragments.specific
import androidx.fragment.app.Fragment
import com.example.lvappquiz.questions.QuestionType
import com.example.lvappquiz.questions.fragments.AbstractQuestionsFragment
import com.example.lvappquiz.questions.fragments.ImageQuestionTextAnswersFragment
import com.example.lvappquiz.questions.fragments.TextQuestionImageAnswersFragment

class GuessAuthorByPictureQuestionsFragment : AbstractQuestionsFragment() {

    override val questionType: QuestionType = QuestionType.GuessAuthorByPicture

    override fun getQuestionFragment(): Fragment = ImageQuestionTextAnswersFragment()
}