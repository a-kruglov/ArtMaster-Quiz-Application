package com.example.lvappquiz.questions.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.lvappquiz.R
import com.example.lvappquiz.questions.QuestionViewModel

class  TextQuestionTextAnswersFragment : Fragment() {

    private lateinit var questionText: TextView // Поле для отображения текста вопроса
    private lateinit var answers: Array<Button> // Массив кнопок для отображения вариантов ответов

    private val viewModel: QuestionViewModel by activityViewModels() // Получение экземпляра QuestionViewModel с использованием activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_text_question_text_answers, container, false) // Инфлейт разметки фрагмента
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        questionText = view.findViewById(R.id.question_text) // Инициализация поля для отображения текста вопроса

        val answersIds = resources.obtainTypedArray(R.array.answers_ids) // Получение массива идентификаторов кнопок ответов из ресурсов

        answers = Array(answersIds.length()) { i ->
            view.findViewById(answersIds.getResourceId(i, -1)) // Инициализация массива кнопок ответов по их идентификаторам
        }

        answersIds.recycle() // Освобождение ресурсов

        for (i in answers.indices) {
            answers[i].setOnClickListener { onAnswerClick(i) } // Установка слушателя кликов для кнопок ответов
        }

        updateView() // Обновление отображения фрагмента
    }

    private fun updateView() {
        setQuestionText() // Установка текста вопроса
        setAnswers() // Установка вариантов ответов
    }

    private fun onAnswerClick(index: Int) {
        viewModel.onAnswerSelected.value = index // Установка выбранного ответа в QuestionViewModel
    }

    private fun setQuestionText() {
        questionText.text = viewModel.question.value?.questionText // Установка текста вопроса
    }

    private fun setAnswers() {
        for (i in answers.indices) {
            answers[i].text = viewModel.question.value?.answers?.get(i) // Установка текста для кнопок ответов

//            //Just for debugging, delete this later
//            if(i == viewModel.question.value?.correctAnswerIndex) {
//                answers[i].text = "${ answers[i].text} (✓)"
//            }
        }
    }
}
