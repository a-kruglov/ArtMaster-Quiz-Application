package com.example.lvappquiz.quiz
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lvappquiz.R
import com.example.lvappquiz.lives.LivesViewModel
import com.example.lvappquiz.questions.QuestionType

class QuizMenuFragment : Fragment() {

    private val livesViewModel: LivesViewModel by activityViewModels()
    private val quizViewModel: QuizViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quiz_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonGuessPictureByAuthor = view.findViewById<Button>(R.id.button_guess_picture_by_author)
        val buttonGuessAuthorByPicture = view.findViewById<Button>(R.id.button_guess_author_by_picture)
        val buttonGuessYearByPicture = view.findViewById<Button>(R.id.button_guess_year_by_picture)
        val buttonGuessBirthYearOfAuthor = view.findViewById<Button>(R.id.button_guess_author_by_fact)

        buttonGuessPictureByAuthor.setOnClickListener { translateToQuestionContainer(QuestionType.GuessPictureByAuthor) }
        buttonGuessAuthorByPicture.setOnClickListener { translateToQuestionContainer(QuestionType.GuessAuthorByPicture) }
        buttonGuessYearByPicture.setOnClickListener { translateToQuestionContainer(QuestionType.GuessYearByPicture) }
        buttonGuessBirthYearOfAuthor.setOnClickListener { translateToQuestionContainer(QuestionType.GuessBirthYearOfAuthor) }
    }

    private fun translateToQuestionContainer(questionType: QuestionType) {
        livesViewModel.reset();
        quizViewModel.questionType.value = questionType
        findNavController().navigate(R.id.action_quizMenuFragment_to_containerFragment)
    }
}
