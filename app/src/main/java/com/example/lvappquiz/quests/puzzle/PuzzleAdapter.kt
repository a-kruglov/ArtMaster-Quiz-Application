package com.example.lvappquiz.quests.puzzle
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lvappquiz.R
import java.util.Collections

class PuzzleAdapter(
    private var puzzlePieces: List<PuzzlePiece>,
    private val originalPuzzlePieces: List<PuzzlePiece>,
    private val isPuzzleSolved: (List<PuzzlePiece>, List<PuzzlePiece>) -> Boolean
) : RecyclerView.Adapter<PuzzleAdapter.PuzzleViewHolder>() {
    private var selectedPiecePosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PuzzleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.puzzle_piece_item, parent, false)
        return PuzzleViewHolder(view)
    }

    override fun onBindViewHolder(holder: PuzzleViewHolder, position: Int) {
        val puzzlePiece = puzzlePieces[position]
        holder.bind(puzzlePiece)

        holder.itemView.setOnClickListener {
            handlePieceClick(holder.adapterPosition, holder)
        }
    }

    private fun handlePieceClick(position: Int, holder: PuzzleViewHolder) {
        if (selectedPiecePosition == null) {
            highlightSelectedPiece(position, holder)
        } else {
            swapPuzzlePieces(selectedPiecePosition!!, position)
            notifyDataSetChanged()

            if (isPuzzleSolved(puzzlePieces, originalPuzzlePieces)) {
                Toast.makeText(holder.itemView.context, "Puzzle Solved", Toast.LENGTH_SHORT).show()
            }
            selectedPiecePosition = null
        }
    }

    private fun highlightSelectedPiece(position: Int, holder: PuzzleViewHolder) {
        selectedPiecePosition = position
        holder.itemView.setBackgroundColor(Color.YELLOW)
    }

    private fun swapPuzzlePieces(firstPosition: Int, secondPosition: Int) {
        Collections.swap(puzzlePieces, firstPosition, secondPosition)
    }

    fun swapItems(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                Collections.swap(puzzlePieces, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                Collections.swap(puzzlePieces, i, i - 1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun getItemCount(): Int = puzzlePieces.size

    inner class PuzzleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val puzzlePieceImage: ImageView = itemView.findViewById(R.id.puzzle_piece_image)

        fun bind(puzzlePiece: PuzzlePiece) {
            puzzlePieceImage.setImageBitmap(puzzlePiece.image)
        }
    }
}
