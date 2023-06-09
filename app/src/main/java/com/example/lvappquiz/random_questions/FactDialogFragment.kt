package com.example.lvappquiz.random_questions
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.lvappquiz.R

interface DialogCloseListener {
    fun onDialogClose()
}

class FactDialogFragment : DialogFragment() {

    var dialogCloseListener: DialogCloseListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factTextView: TextView = view.findViewById(R.id.fact_text_view)
        val fact = arguments?.getString("fact")
        fact?.let {
            factTextView.text = it
        }

        val okayButton: Button = view.findViewById(R.id.okay_button)
        okayButton.setOnClickListener {
            dismiss()
        }
    }

    override fun onStart() {
        super.onStart()

        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog.window?.setLayout(width, height)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dialogCloseListener?.onDialogClose()
    }
}
