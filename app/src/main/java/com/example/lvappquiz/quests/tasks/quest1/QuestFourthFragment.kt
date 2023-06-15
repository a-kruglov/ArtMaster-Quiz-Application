package com.example.lvappquiz.quests.tasks.quest1
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.lvappquiz.R
import com.example.lvappquiz.quests.puzzle.PuzzleAdapter
import com.example.lvappquiz.quests.puzzle.PuzzlePiece
import com.example.lvappquiz.quests.puzzle.PuzzleTouchHelperCallback
import com.example.lvappquiz.quests.tasks.AbstractTaskFragment

class QuestFourthFragment : AbstractTaskFragment() {

    private lateinit var puzzleAdapter: PuzzleAdapter
    private lateinit var originalPuzzlePieces: List<PuzzlePiece>

    override val fragmentId: Int = R.layout.fragment_quest_fourth

    private val fact = "В 25 зале Вы можете насладиться картиной, которую  из-за сладостей, на которых изображена репродукция Шишкина, некоторые россияне сейчас называют — «Три медведя». Такое ошибочное название появилось из-за того, что на некоторых обертках конфет и шоколадок нарисованы трое, а не четверо медведя."
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val puzzlePieces = createAndShufflePuzzlePieces()
        setupPuzzleRecyclerView(puzzlePieces)
    }
    private fun createAndShufflePuzzlePieces(): MutableList<PuzzlePiece> {
        val image = BitmapFactory.decodeResource(resources, R.drawable.puzzle)
        val scaledImage = Bitmap.createScaledBitmap(image, 300, 300, true)

        val puzzlePieces = createPuzzlePieces(scaledImage, 4, 4)
        originalPuzzlePieces = puzzlePieces.toList()
        puzzlePieces.shuffle()
        return puzzlePieces
    }
    private fun createPuzzlePieces(image: Bitmap, rows: Int, cols: Int): MutableList<PuzzlePiece> {
        val pieceWidth = image.width / cols
        val pieceHeight = image.height / rows

        return MutableList(rows * cols) {
            val x = (it % cols) * pieceWidth
            val y = (it / cols) * pieceHeight
            PuzzlePiece(Bitmap.createBitmap(image, x, y, pieceWidth, pieceHeight))
        }
    }
    private fun setupPuzzleRecyclerView(puzzlePieces: MutableList<PuzzlePiece>) {
        view?.findViewById<RecyclerView>(R.id.puzzle_recycler_view)?.apply {
            layoutManager = GridLayoutManager(context, 4)
            puzzleAdapter = PuzzleAdapter(puzzlePieces, originalPuzzlePieces, ::checkForPuzzleIsSolved)
            adapter = puzzleAdapter
            attachItemTouchHelper()
            addPaddingToItems()
        }
    }
    private fun RecyclerView.attachItemTouchHelper() {
        ItemTouchHelper(PuzzleTouchHelperCallback(puzzleAdapter)).attachToRecyclerView(this)
    }
    private fun RecyclerView.addPaddingToItems() {
        addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.set(2, 2, 2, 2)
            }
        })
    }
    private fun checkForPuzzleIsSolved(currentPieces: List<PuzzlePiece>, originalPieces: List<PuzzlePiece>): Boolean {
        val puzzleSolved = currentPieces == originalPieces
        if (puzzleSolved) {
            onPuzzleSolved()
        }
        return puzzleSolved
    }
    private fun onPuzzleSolved() {
        showDialog(fact)
    }
}
