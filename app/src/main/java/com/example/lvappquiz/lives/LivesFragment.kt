package com.example.lvappquiz.lives
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import com.example.lvappquiz.R

class LivesFragment : Fragment() {

    private lateinit var lives: Array<ImageView>
    private val viewModel: LivesViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lives, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val livesIds = resources.obtainTypedArray(R.array.lives_ids)

        lives = Array(livesIds.length()) { i ->
            view.findViewById(livesIds.getResourceId(i, -1))
        }

        viewModel.lives.observe(viewLifecycleOwner) { onLivesChanged(it) }
        livesIds.recycle()
    }
    private fun onLivesChanged(newLives: Int) {
        if (newLives > 0) {
            for (i in 0 until newLives) {
                lives[i].visibility = View.VISIBLE
            }
            for (i in newLives until lives.size) {
                lives[i].visibility = View.INVISIBLE
            }
        }
    }
}

