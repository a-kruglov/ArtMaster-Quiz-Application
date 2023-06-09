package com.example.lvappquiz.questions.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.lvappquiz.R
import com.example.lvappquiz.questions.QuestionViewModel

//Это фрагмент, отображающий вопрос с изображением и текстом вариантов ответов.
// Он использует данные из QuestionViewModel для заполнения полей вопроса и обрабатывает
// выбор пользователя, устанавливая выбранный ответ в QuestionViewModel. Он также использует
// библиотеку Glide для загрузки и отображения изображения вопроса.

class ImageQuestionTextAnswersFragment : Fragment() {

    private lateinit var questionImage: ImageView
    private lateinit var questionText: TextView // Поле для отображения текста вопроса
    private lateinit var answers: Array<Button> // Массив кнопок для ответов

    private val viewModel: QuestionViewModel by activityViewModels() // Получение экземпляра QuestionViewModel с использованием activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_question_text_answers_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        questionImage = view.findViewById(R.id.question_image) // Инициализация поля для отображения изображения вопроса
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
        setQuestionImage() // Установка изображения вопроса, если оно есть
        setQuestionText() // Установка текста вопроса
        setAnswers() // Установка текста для кнопок ответов
    }

    private fun onAnswerClick(index: Int) {
        viewModel.onAnswerSelected.value = index // Установка выбранного ответа в QuestionViewModel
    }

    private fun setQuestionImage() {
        if(viewModel.question.value?.image == null){
            return
        }
        Glide.with(this)
            .load(viewModel.question.value?.image)
            .into(questionImage) // Загрузка и отображение изображения вопроса с использованием библиотеки Glide
    }

    private fun setQuestionText() {
        questionText.text = viewModel.question.value?.questionText // Установка текста вопроса
    }

    private fun setAnswers() {
        for (i in answers.indices) {
            answers[i].text = viewModel.question.value?.answers?.get(i) // Установка текста для кнопок ответов

//            // Просто для отладки, позже удалить
//            if(i == viewModel.question.value?.correctAnswerIndex) {
//                answers[i].text = "${ answers[i].text} (✓)" // Добавление символа (✓) к правильному ответу для отладки
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Glide.with(this).clear(questionImage) // Очистка ресурсов связанных с загруженным изображением при уничтожении представления фрагмента
    }
}

