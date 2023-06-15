package com.example.lvappquiz.quests
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.lvappquiz.R
import com.example.lvappquiz.quests.data.TaskData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QuestContainerFragment : Fragment() {

    private val viewModel: QuestViewModel by activityViewModels()
    private var currentTask : TaskData? = null
    private var currentTaskObserver: Observer<Boolean>? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quest_container, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        processNextTask();
    }
    private fun processNextTask() {
        val task = viewModel.getFirstNotCompletedTask()
        if (task == null) {
            finish()
        } else {
            setupFragmentWithTask(task)
        }
    }
    private fun startObservingTasksCompletion(task : TaskData) {
        currentTaskObserver = Observer { isCompleted ->
            if (isCompleted) {
                onTaskCompleted(task)
            }
        }
        currentTask?.isCompleted?.observe(viewLifecycleOwner, currentTaskObserver!!)
    }
    private fun onTaskCompleted(taskData: TaskData) {
        if(currentTask != taskData){
            return
        }
        processNextTask()
    }
    private fun setupFragmentWithTask(task: TaskData) {
        currentTask = task
        startObservingTasksCompletion(task)

        val fragment: Fragment = task.fragmentClass.newInstance()
        val args = Bundle()

        args.putInt("task_index", viewModel.getTaskIndex(task))
        fragment.arguments = args

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.quest_container, fragment)
        transaction.commit()
    }
    private fun finish() {
        CoroutineScope(Dispatchers.Main).launch {
            findNavController().navigateUp()
        }
    }
}
