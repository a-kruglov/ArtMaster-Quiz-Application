package com.example.lvappquiz.quests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.lvappquiz.R
import com.example.lvappquiz.databinding.FragmentQuestFirstBinding
import java.util.*


class QuestFirstFragment : Fragment() {
    private lateinit var binding: FragmentQuestFirstBinding
    private val correctAnswer = "русалки"
    var nextFragmentListener: NextFragmentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            val userAnswer = binding.editText.text.toString().trim().lowercase(Locale.getDefault())

            if (isAnswerCorrect(userAnswer)) {
                (parentFragment as? QuestContainerFragment)?.showFactDialog(requireContext().getString(R.string.fact_text))
            } else {
                showAnswerError()
            }
        }
    }

    fun isAnswerCorrect(userAnswer: String): Boolean {
        return userAnswer.equals(correctAnswer, ignoreCase = true)
    }

    fun showAnswerError() {
        binding.textViewError.isVisible = true
        binding.textViewError.animate().alpha(1f).setDuration(300).withEndAction {
            binding.textViewError.animate().alpha(0f).setDuration(300).withEndAction {
                binding.textViewError.isVisible = false
                binding.editText.text = null
            }
        }
    }
    interface NextFragmentListener {
        fun onNextFragment()
    }

}

