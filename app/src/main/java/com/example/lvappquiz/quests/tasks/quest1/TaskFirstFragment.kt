package com.example.lvappquiz.quests.tasks.quest1
import com.example.lvappquiz.R
import com.example.lvappquiz.quests.tasks.AbstractTextQuestionFragment

class TaskFirstFragment : AbstractTextQuestionFragment() {

    override val fragmentId: Int = R.layout.fragment_quest_first
    override fun getCorrectAnswer() = "русалки"
    override fun getFact() : String {
        return requireContext().getString(R.string.fact_text)
    }
}

