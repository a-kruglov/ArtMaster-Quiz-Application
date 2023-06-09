package com.example.lvappquiz.quests

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lvappquiz.R
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.lvappquiz.quests.puzzle.PuzzleAdapter
import com.example.lvappquiz.quests.puzzle.PuzzlePiece
import com.example.lvappquiz.quests.puzzle.PuzzleTouchHelperCallback

class QuestFourthFragment : Fragment() {

    private lateinit var puzzleAdapter: PuzzleAdapter
    private lateinit var originalPuzzlePieces: List<PuzzlePiece>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_quest_fourth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val originalImage = BitmapFactory.decodeResource(resources, R.drawable.puzzle)
        val scaledImage = Bitmap.createScaledBitmap(originalImage, 300, 300, true)
        val puzzlePieces = createPuzzlePieces(scaledImage, 4, 4)
        val mutablePuzzlePieces = puzzlePieces.toMutableList()
        originalPuzzlePieces = puzzlePieces.toList()
        mutablePuzzlePieces.shuffle()

        setupPuzzleRecyclerView(mutablePuzzlePieces)
    }

    fun isPuzzleSolved(
        currentPieces: List<PuzzlePiece>,
        originalPieces: List<PuzzlePiece>
    ): Boolean {
        return currentPieces == originalPieces
    }


    private fun createPuzzlePieces(image: Bitmap, rows: Int, cols: Int): List<PuzzlePiece> {
        val pieces = mutableListOf<PuzzlePiece>()
        val pieceWidth = image.width / cols
        val pieceHeight = image.height / rows

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val x = col * pieceWidth
                val y = row * pieceHeight
                val pieceImage = Bitmap.createBitmap(image, x, y, pieceWidth, pieceHeight)
                pieces.add(PuzzlePiece(pieceImage))
            }
        }

        return pieces
    }

    private fun setupPuzzleRecyclerView(puzzlePieces: MutableList<PuzzlePiece>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.puzzle_recycler_view)
        recyclerView?.apply {
            layoutManager = GridLayoutManager(context, 4)
            puzzleAdapter = PuzzleAdapter(puzzlePieces, originalPuzzlePieces, ::isPuzzleSolved)
            adapter = puzzleAdapter

            val callback = PuzzleTouchHelperCallback(puzzleAdapter)
            val touchHelper = ItemTouchHelper(callback)
            touchHelper.attachToRecyclerView(this)

            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    outRect.set(2, 2, 2, 2)
                }
            })
        }
    }
}