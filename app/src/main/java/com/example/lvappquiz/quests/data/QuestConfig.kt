package com.example.lvappquiz.quests.data

import com.example.lvappquiz.quests.tasks.quest1.TaskFirstFragment
import com.example.lvappquiz.quests.tasks.quest1.QuestFourthFragment
import com.example.lvappquiz.quests.tasks.quest1.QuestSecondFragment

class QuestConfig {

    //пока просто один квест - три задания
    val quest : QuestData = QuestData(
        listOf(
            TaskData(TaskFirstFragment::class.java),
            TaskData(QuestSecondFragment::class.java),
            TaskData(QuestFourthFragment::class.java)
        )
    )
}