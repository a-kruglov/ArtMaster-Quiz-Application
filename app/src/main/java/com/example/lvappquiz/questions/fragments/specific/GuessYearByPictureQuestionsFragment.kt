package com.example.lvappquiz.questions.fragments.specific
import androidx.fragment.app.Fragment
import com.example.lvappquiz.questions.QuestionType
import com.example.lvappquiz.questions.fragments.AbstractQuestionsFragment
import com.example.lvappquiz.questions.fragments.ImageQuestionTextAnswersFragment
import com.example.lvappquiz.questions.fragments.TextQuestionImageAnswersFragment

class GuessYearByPictureQuestionsFragment : AbstractQuestionsFragment() {

    override val questionType: QuestionType = QuestionType.GuessYearByPicture // Установка типа вопроса для этого фрагмента

    override fun getQuestionFragment(): Fragment = ImageQuestionTextAnswersFragment() // Возвращение фрагмента для отображения вопроса с картинкой и вариантами ответов в виде текста
}
