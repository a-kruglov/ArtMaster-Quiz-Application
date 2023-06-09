package com.example.lvappquiz.menu
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.lvappquiz.R

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonQuiz = view.findViewById<Button>(R.id.button_quiz)
        val buttonQuest = view.findViewById<Button>(R.id.button_quests)
        val buttonRandomQuestions = view.findViewById<Button>(R.id.button_random)
        val buttonAiGuess = view.findViewById<Button>(R.id.button_ai_guess)

        buttonQuiz.setOnClickListener { openQuiz() }
        buttonQuest.setOnClickListener { openQuests() }
        buttonRandomQuestions.setOnClickListener { openRandomQuestions() }
        buttonAiGuess.setOnClickListener { openButtonAiGuess() }
    }

    private fun openQuiz() { findNavController().navigate(R.id.action_menuFragment_to_quizMenuFragment) }
    private fun openQuests() { findNavController().navigate(R.id.action_menuFragment_to_questContainerFragment) }
    private fun openRandomQuestions() { findNavController().navigate(R.id.actionMenuFragmentToRandomQuestionsFragment) }
    private fun openButtonAiGuess() { findNavController().navigate(R.id.action_menuFragment_to_aiGuess) }
}
