package com.example.lvappquiz.quests.data
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData

data class QuestData(val tasks: List<TaskData>)

data class TaskData(val fragmentClass: Class<out Fragment>, var isCompleted: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false))
