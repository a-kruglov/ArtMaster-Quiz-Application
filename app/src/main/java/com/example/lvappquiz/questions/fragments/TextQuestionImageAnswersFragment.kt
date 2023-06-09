package com.example.lvappquiz.questions.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.lvappquiz.R
import com.example.lvappquiz.questions.QuestionViewModel

class TextQuestionImageAnswersFragment : Fragment() { //Это фрагмент, отображающий вопрос с текстом и
// изображениями вариантов ответов. Он использует данные из QuestionViewModel для заполнения полей вопроса
// и обрабатывает выбор пользователя, устанавливая выбранный ответ в QuestionViewModel.


        private lateinit var questionText: TextView // Поле для отображения текста вопроса
        private lateinit var answerImages: Array<ImageView> // Массив полей для отображения изображений ответов

        private val viewModel: QuestionViewModel by activityViewModels() // Получение экземпляра QuestionViewModel с использованием activityViewModels()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_image_question_image_answers_fragment, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            questionText = view.findViewById(R.id.question_text) // Инициализация поля для отображения текста вопроса

            val answerImageIds = resources.obtainTypedArray(R.array.answer_image_ids) // Получение массива идентификаторов полей для отображения изображений ответов из ресурсов

            answerImages = Array(answerImageIds.length()) { i ->
                view.findViewById(answerImageIds.getResourceId(i, -1)) as ImageView // Инициализация массива полей для отображения изображений ответов по их идентификаторам
            }

            answerImageIds.recycle() // Освобождение ресурсов

            for (i in answerImages.indices) {
                answerImages[i].setOnClickListener { onAnswerClick(i) } // Установка слушателя кликов для полей отображения изображений ответов
            }

            updateView() // Обновление отображения фрагмента
        }

        private fun updateView() {
            setQuestionText() // Установка текста вопроса
            setAnswerImages() // Установка изображений ответов
        }

        private fun onAnswerClick(index: Int) {
            viewModel.onAnswerSelected.value = index // Установка выбранного ответа в QuestionViewModel
        }

        private fun setQuestionText() {
            questionText.text = viewModel.question.value?.questionText // Установка текста вопроса
        }

        private fun setAnswerImages() {
            for (i in answerImages.indices) {
                Glide.with(this)
                    .load(viewModel.question.value?.answers?.get(i))
                    .into(answerImages[i]) // Загрузка и отображение изображений ответов с использованием библиотеки Glide
            }
        }

        override fun onDestroyView() {
            super.onDestroyView()
            for (i in answerImages.indices) {
                Glide.with(this).clear(answerImages[i]) // Очистка ресурсов связанных с загруженными изображениями при уничтожении представления фрагмента
            }
        }
    }
