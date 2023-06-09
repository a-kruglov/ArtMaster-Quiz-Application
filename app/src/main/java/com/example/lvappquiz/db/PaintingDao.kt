package com.example.lvappquiz.db

import androidx.room.*

@Dao
interface PaintingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(paintings: List<Painting>)

    @Query("SELECT * FROM paintings")
    fun getAllPaintings(): List<Painting>

    @Query("SELECT * FROM paintings WHERE id = :paintingId")
    fun getPaintingById(paintingId: Int): Painting?

    @Query("SELECT DISTINCT * FROM paintings ORDER BY RANDOM() LIMIT :limit")
    fun getRandomPaintings(limit: Int): List<Painting>

    @Query("SELECT DISTINCT * FROM paintings WHERE id != :ignorePainting ORDER BY RANDOM() LIMIT :limit")
    fun getRandomPaintings(limit: Int, ignorePainting: Int): List<Painting>

    @Query("SELECT DISTINCT * FROM paintings WHERE id NOT IN (:ignorePaintings) ORDER BY RANDOM() LIMIT :limit")
    fun getRandomPaintings(limit: Int, ignorePaintings: List<Int>): List<Painting>

    @Query("SELECT DISTINCT year FROM paintings WHERE year != :ignoreYear ORDER BY RANDOM() LIMIT :limit")
    fun getRandomYears(limit: Int, ignoreYear: Int): List<Int>

    @Query("SELECT DISTINCT * FROM paintings WHERE authorId != :ignoreAuthor ORDER BY RANDOM() LIMIT :limit")
    fun getRandomPaintingsIgnoringAuthor(limit: Int, ignoreAuthor: Int): List<Painting>

    @Query("SELECT history FROM paintings WHERE id = :paintingId")
    fun getPaintingHistoryById(paintingId: Int): String?

}
