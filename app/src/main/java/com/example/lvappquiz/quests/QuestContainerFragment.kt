package com.example.lvappquiz.quests

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lvappquiz.R
import com.example.lvappquiz.databinding.FragmentQuestContainerBinding
import com.example.lvappquiz.dialogues.FactDialogFragment

class QuestContainerFragment : Fragment(), QuestFirstFragment.NextFragmentListener,
    FactDialogFragment.DialogCloseListener {

    private lateinit var binding: FragmentQuestContainerBinding
    private val fragmentQuestFirst = QuestFirstFragment()
    private val fragmentQuestSecond = QuestSecondFragment()
    private val fragmentQuestFouth = QuestFourthFragment()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showFragment(fragmentQuestFirst)
        fragmentQuestFirst.nextFragmentListener = this
    }

    private fun showFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.quest_container, fragment)
            .commit()
    }


    fun showFactDialog(factText: String) {
        val factDialogFragment = FactDialogFragment()
        val args = Bundle().apply {
            putString("fact", factText)
        }
        factDialogFragment.arguments = args
        factDialogFragment.dialogCloseListener = this
        factDialogFragment.show(childFragmentManager, "factDialog")
    }


    override fun onNextFragment() {
        showFragment(fragmentQuestSecond)
    }

    override fun onDialogClose() {
        val currentFragment = childFragmentManager.findFragmentById(R.id.quest_container)

        when (currentFragment) {
            is QuestFirstFragment -> {
                showFragment(fragmentQuestSecond)
            }
            is QuestSecondFragment -> {
                showFragment(fragmentQuestFouth)
            }
        }
    }
}
