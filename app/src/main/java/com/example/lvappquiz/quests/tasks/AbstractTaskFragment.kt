package com.example.lvappquiz.quests.tasks
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.lvappquiz.R
import com.example.lvappquiz.dialogues.DialogCloseListener
import com.example.lvappquiz.dialogues.TextDialogFragment
import com.example.lvappquiz.quests.QuestViewModel

abstract class AbstractTaskFragment : Fragment(), DialogCloseListener {

    private lateinit var textViewError: TextView

    protected val viewModel: QuestViewModel by activityViewModels()

    private var currentTaskIndex : Int? = null

    abstract val fragmentId : Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(fragmentId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewError = view.findViewById(R.id.textViewError)
        currentTaskIndex = arguments?.getInt("task_index")
    }

    protected fun showDialog(text : String) {
        val textDialogFragment = TextDialogFragment()
        val args = Bundle().apply {
            putString("text", text)
        }
        textDialogFragment.arguments = args
        textDialogFragment.dialogCloseListener = this
        textDialogFragment.show(childFragmentManager, "factDialog")
    }

    protected fun showAnswerError() {
        textViewError.isVisible = true
        textViewError.animate().alpha(1f).setDuration(300).withEndAction {
            textViewError.animate().alpha(0f).setDuration(300).withEndAction {
                textViewError.isVisible = false
            }
        }
    }

    override fun onDialogClose() {
        complete()
    }

    protected fun complete() {
        viewModel.completeTaskWithIndex(currentTaskIndex!!)
    }
}