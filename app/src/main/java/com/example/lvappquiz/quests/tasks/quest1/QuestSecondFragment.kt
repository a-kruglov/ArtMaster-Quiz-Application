package com.example.lvappquiz.quests.tasks.quest1
import com.example.lvappquiz.R
import com.example.lvappquiz.quests.tasks.AbstractTextQuestionFragment

class QuestSecondFragment : AbstractTextQuestionFragment() {
    override val fragmentId: Int = R.layout.fragment_quest_second

    override fun getCorrectAnswer() = "незнакомка"

    override fun getFact() : String {
        return requireContext().getString(R.string.fact_text)
    }
}
