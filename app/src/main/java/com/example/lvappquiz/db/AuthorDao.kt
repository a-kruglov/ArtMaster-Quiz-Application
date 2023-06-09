package com.example.lvappquiz.db

import androidx.room.*

@Dao
interface AuthorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(authors: List<Author>)

    @Transaction
    @Query("SELECT * FROM authors")
    fun getAuthorsWithPaintings(): List<AuthorWithPaintings>

    @Query("SELECT * FROM authors")
    fun getAllAuthors(): List<Author>

    @Query("SELECT * FROM authors WHERE id = :authorId")
    fun getAuthorById(authorId: Int): Author?

    @Query("SELECT DISTINCT * FROM authors ORDER BY RANDOM() LIMIT :limit")
    fun getRandomAuthors(limit: Int): List<Author>

    @Query("SELECT DISTINCT * FROM authors WHERE id != :ignoreAuthor ORDER BY RANDOM() LIMIT :limit")
    fun getRandomAuthors(limit: Int, ignoreAuthor: Int): List<Author>

    @Query("SELECT DISTINCT * FROM authors WHERE id NOT IN (:ignoreAuthors) ORDER BY RANDOM() LIMIT :limit")
    fun getRandomAuthors(limit: Int, ignoreAuthors: List<Int>): List<Author>

    @Query("SELECT DISTINCT birthYear FROM authors WHERE birthYear != :ignoreYear ORDER BY RANDOM() LIMIT :limit")
    fun getRandomBirthYears(limit: Int, ignoreYear: Int): List<Int>
}
