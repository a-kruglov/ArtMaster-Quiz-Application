package com.example.lvappquiz.questions.fragments
import ProgressSharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.lvappquiz.R
import com.example.lvappquiz.dialogues.DialogCloseListener
import com.example.lvappquiz.dialogues.TextDialogFragment
import com.example.lvappquiz.lives.LivesFragment
import com.example.lvappquiz.lives.LivesViewModel
import com.example.lvappquiz.questions.QuestionGenerator
import com.example.lvappquiz.questions.QuestionType
import com.example.lvappquiz.questions.QuestionViewModel
import com.example.lvappquiz.user_progress.ProgressFragment
import com.example.lvappquiz.user_progress.ProgressViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class AbstractQuestionsFragment : Fragment(), DialogCloseListener {

    private lateinit var progressSharedPreferences: ProgressSharedPreferences

    protected val questionViewModel: QuestionViewModel by activityViewModels()
    private val livesViewModel: LivesViewModel by activityViewModels()
    private val progressViewModel: ProgressViewModel by activityViewModels()

    private var needToShowRecordDialog = false

    private lateinit var questionGenerator: QuestionGenerator

    protected abstract val questionType : QuestionType

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        questionGenerator = QuestionGenerator(questionType, requireContext())
        progressSharedPreferences = ProgressSharedPreferences(requireContext())
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLivesFragment()
        setupProgressFragment()
        setupNextQuestion() // Начало игры: получение первого вопроса и отображение его фрагмента

        questionViewModel.onAnswerSelected.observe(viewLifecycleOwner) { onAnswerClick(it) }
        Log.d("AbstractQuestionsFragment", "${questionViewModel.onAnswerSelected.value}")
    }

    private fun setupLivesFragment() {
        childFragmentManager.beginTransaction()
            .replace(R.id.questionLivesFragmentContainer, LivesFragment())
            .commit()
    }

    private fun setupProgressFragment() {
        val record = progressSharedPreferences.getProgress(questionType)
        val progressFragment = ProgressFragment().apply {
            arguments = Bundle().apply {
                putInt("record", record)
            }
        }
        childFragmentManager.beginTransaction()
            .replace(R.id.questionProgressFragmentContainer, progressFragment)
            .commit()
    }

    private fun onAnswerClick(index: Int?) {
        if (index == null) {
            return
        }
        if (index != questionViewModel.question.value?.correctAnswerIndex) {
            processLiveDecreasing() // Обработка неправильного ответа: уменьшение количества жизней
            Toast.makeText(requireContext(), "Неправильно", Toast.LENGTH_SHORT).show()

        } else {
            increaseProgress()
            Toast.makeText(requireContext(), "Правильно", Toast.LENGTH_SHORT).show()
            setupNextQuestion() // Обработка правильного ответа: получение следующего вопроса и отображение его фрагмента
        }
    }

    private fun processLiveDecreasing() {
        livesViewModel.lives.value = livesViewModel.lives.value?.minus(1) // Уменьшение ;bpyb
        val fact = questionViewModel.question.value?.fact
        if (fact != null) {
            openFactDialog(fact) // ответ правильный, открываем диалоговое окно с фактом
        } else {
            checkLivesAndProceed()
        }
    }

    private fun increaseProgress() {
        val newProgress = progressViewModel.progress.value?.plus(1)
        progressViewModel.progress.value = newProgress
        saveRecordProgress(newProgress ?: 0)
    }

    private fun saveRecordProgress(newProgress: Int) {
        val currentRecord = progressSharedPreferences.getProgress(questionType)
        if(newProgress > currentRecord) {
            needToShowRecordDialog = true
            progressSharedPreferences.saveProgress(questionType, newProgress)
        }
    }

    private fun checkLivesAndProceed() {
        if (livesViewModel.lives.value!! <= 0) {
            finishTheGame() // Если количество жизней равно или меньше нуля, завершаем игру
        } else {
            setupNextQuestion() // В противном случае получаем следующий вопрос и отображаем его фрагмент
        }
    }

    private fun openFactDialog(text : String, withCallback : Boolean = true) {
        val textDialogFragment = TextDialogFragment()
        val args = Bundle()
        args.putString("text", text)
        textDialogFragment.arguments = args
        if(withCallback){
            textDialogFragment.dialogCloseListener = this
        }
        textDialogFragment.show(requireActivity().supportFragmentManager, "factDialog")
    }

    override fun onDialogClose() {
        checkLivesAndProceed() // При закрытии диалогового окна проверяем количество жизней и продолжаем игру
    }

    private fun setupNextQuestion() {
        CoroutineScope(Dispatchers.IO).launch {
            val question = questionGenerator.getQuestion() // Получение следующего вопроса с помощью генератора вопросов
            withContext(Dispatchers.Main) {
                questionViewModel.question.value = question // Установка вопроса в QuestionViewModel для отображения
                updateQuestionFragment() // Обновление фрагмента с вопросом
            }
        }
    }

    private fun updateQuestionFragment() {
        val fragment = getQuestionFragment() // Получение фрагмента с вопросом

        childFragmentManager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.questionFragmentContainer, fragment)
            .commit()
    }

    protected abstract fun getQuestionFragment(): Fragment // Абстрактная функция для получения фрагмента с вопросом

    private fun finishTheGame() {
        val record = progressSharedPreferences.getProgress(questionType)
        if(needToShowRecordDialog) {
            openFactDialog("Ты побил свой рекорд!\nНовый рекорд: $record", false)
        }
        CoroutineScope(Dispatchers.Main).launch {
            findNavController().navigateUp() // Возвращение к предыдущему экрану
            resetModelView() // Сброс состояния ViewModel перед закрытием фрагмента
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        resetModelView() // Сброс состояния ViewModel при уничтожении представления фрагмента
    }

    private fun resetModelView() {
        questionViewModel.reset() // Сброс состояния QuestionViewModel
        livesViewModel.reset() // Сброс состояния LivesViewModel
        progressViewModel.reset()
        needToShowRecordDialog = false
    }
}
