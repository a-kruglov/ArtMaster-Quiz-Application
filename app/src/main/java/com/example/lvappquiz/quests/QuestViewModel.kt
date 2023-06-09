package com.example.lvappquiz.quests
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.lvappquiz.quests.data.QuestData
import com.example.lvappquiz.quests.data.TaskData

class QuestViewModel : ViewModel() {

    val quest = MutableLiveData<QuestData?>()

    fun progress(): Int {
        var completed = 0
        quest.value?.tasks?.forEach {
            if (it.isCompleted.value == true) completed++
        }
        return completed
    }

    fun getTaskIndex(task: TaskData): Int {
        return quest.value?.tasks?.indexOf(task) ?: -1
    }

    fun completeTaskWithIndex(index : Int) {
        quest.value?.tasks?.get(index)?.isCompleted?.value = true
    }

    fun getFirstNotCompletedTask(): TaskData? {
        quest.value?.tasks?.forEach {
            if (it.isCompleted.value != true) return it
        }
        return null
    }

    fun reset() {
        quest.value = null
    }
}