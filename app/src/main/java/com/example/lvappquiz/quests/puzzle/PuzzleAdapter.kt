package com.example.lvappquiz.quests.puzzle

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lvappquiz.R
import java.util.*

class PuzzleAdapter(
    private val puzzlePieces: List<PuzzlePiece>,
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
        holder.puzzlePieceImage.setImageBitmap(puzzlePiece.image)

        holder.puzzlePieceImage.setOnClickListener {
            val currentPosition = holder.getAdapterPosition()
            if (selectedPiecePosition == null) {
                selectedPiecePosition = currentPosition
                holder.puzzlePieceImage.setBackgroundColor(Color.YELLOW)
            } else {
                // Добавляем вызов функции swapPuzzlePieces здесь
                swapPuzzlePieces(selectedPiecePosition!!, currentPosition)
                notifyDataSetChanged()

                if (isPuzzleSolved(puzzlePieces, originalPuzzlePieces)) {
                    Toast.makeText(holder.itemView.context, "Пазл собран", Toast.LENGTH_SHORT).show()
                }
                selectedPiecePosition = null
            }
        }
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

    override fun getItemCount(): Int {
        return puzzlePieces.size
    }

    class PuzzleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val puzzlePieceImage: ImageView = itemView.findViewById(R.id.puzzle_piece_image)
    }

}

