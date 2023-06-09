package com.example.lvappquiz.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "paintings", foreignKeys = [ForeignKey(entity = Author::class, parentColumns = ["id"], childColumns = ["authorId"])])
data class Painting(
    @PrimaryKey val id: Int,
    val title: String,
    val year: Int,
    val history: String,
    val style: String,
    val image: String,
    val authorId: Int
)

