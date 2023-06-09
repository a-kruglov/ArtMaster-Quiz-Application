package com.example.lvappquiz.db

import androidx.room.Embedded
import androidx.room.Relation
import com.example.lvappquiz.db.Author
import com.example.lvappquiz.db.Painting

data class AuthorWithPaintings(
    @Embedded val author: Author,
    @Relation(
        parentColumn = "id",
        entityColumn = "authorId"
    )
    val paintings: List<Painting>
)
