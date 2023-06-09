package com.example.lvappquiz.quests.tasks
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.lvappquiz.R
import java.util.Locale

abstract class AbstractTextQuestionFragment : AbstractTaskFragment() {

    private lateinit var button: Button
    private lateinit var editText: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button = view.findViewById(R.id.button)
        editText = view.findViewById(R.id.editText)
        button.setOnClickListener { onButtonClick() }
    }

    protected abstract fun getCorrectAnswer() : String

    protected abstract fun getFact() : String

    private fun getUserAnswer(): String {
        return editText.text.toString().trim().lowercase(Locale.getDefault())
    }

    private fun checkUserAnswer(): Boolean {
        val userAnswer = getUserAnswer()
        val correctAnswer = getCorrectAnswer()
        return userAnswer.equals(correctAnswer, ignoreCase = true)
    }

    private fun onButtonClick() {
        val answerCorrect = checkUserAnswer()
        if (answerCorrect) {
            val fact = getFact()
            if(fact.isEmpty()) {
                complete()
                return
            }
            showDialog(fact)
        } else {
            editText.text = null
            showAnswerError()
        }
    }
}