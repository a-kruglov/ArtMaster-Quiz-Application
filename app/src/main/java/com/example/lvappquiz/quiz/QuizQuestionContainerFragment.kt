package com.example.lvappquiz.quiz
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.lvappquiz.R
import com.example.lvappquiz.questions.QuestionType
import com.example.lvappquiz.questions.fragments.specific.GuessBirthYearOfAuthorQuestionFragment
import com.example.lvappquiz.questions.fragments.specific.GuessAuthorByPictureQuestionsFragment
import com.example.lvappquiz.questions.fragments.specific.GuessPictureByAuthorQuestionsFragment
import com.example.lvappquiz.questions.fragments.specific.GuessYearByPictureQuestionsFragment

class QuizQuestionContainerFragment : Fragment() {

    private val viewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz_question_container, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setQuestionView() //выставляем тип вопроса
    }

    private fun setQuestionView() {
        val questionType = viewModel.questionType.value
        if (questionType != null) {
            when (questionType) {
                QuestionType.GuessPictureByAuthor -> setFragment(GuessPictureByAuthorQuestionsFragment())
                QuestionType.GuessAuthorByPicture -> setFragment(GuessAuthorByPictureQuestionsFragment())
                QuestionType.GuessYearByPicture -> setFragment(GuessYearByPictureQuestionsFragment())
                QuestionType.GuessBirthYearOfAuthor -> setFragment(GuessBirthYearOfAuthorQuestionFragment())
                else -> {}
            }
        }
    }

    private fun setFragment(fragment: Fragment) {
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.quiz_question_container, fragment)
        transaction.commit()
    }
}
