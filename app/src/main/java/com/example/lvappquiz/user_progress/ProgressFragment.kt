package com.example.lvappquiz.user_progress
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.lvappquiz.R

class ProgressFragment : Fragment() {

    private lateinit var progressText: TextView
    private lateinit var recordText: TextView

    private val viewModel: ProgressViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressText = view.findViewById(R.id.progress_text)
        recordText = view.findViewById(R.id.record_text)

        val record = arguments?.getInt("record", 0)
        recordText.text = "Record: $record"

        viewModel.progress.observe(viewLifecycleOwner) { onProgressChanged(it) }
    }

    private fun onProgressChanged(newProgress: Int) {
        progressText.text = "Progress: $newProgress"
    }
}

