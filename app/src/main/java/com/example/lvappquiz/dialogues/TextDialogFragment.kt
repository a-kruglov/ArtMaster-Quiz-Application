package com.example.lvappquiz.dialogues

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.lvappquiz.databinding.FragmentFactBinding

interface DialogCloseListener {
    fun onDialogClose()
}
class TextDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentFactBinding
    var dialogCloseListener: DialogCloseListener? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFactBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fact = arguments?.getString("text")
        Log.d("FactDialogFragment", "Текст факта: $fact")

        binding.factTextView.text = fact

        binding.okayButton.setOnClickListener {
            dismissAllowingStateLoss()
            dialogCloseListener?.onDialogClose()
        }
    }
    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
        }
    }
}

