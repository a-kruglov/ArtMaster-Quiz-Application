package com.example.lvappquiz
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.lvappquiz.db.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
    private lateinit var paintingDao: PaintingDao
    private lateinit var authorDao: AuthorDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        paintingDao = db.paintingDao()
        authorDao = db.authorDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndReadPainting() {
        val author = Author(1, "Леонардо да Винчи", 1452, "Художник эпохи Возрождения")
        authorDao.insertAll(listOf(author))

        val painting = Painting(1, "Мона Лиза", 1503, "Знаменитый портрет", "Возрождение", "изображение", 1)
        paintingDao.insertAll(listOf(painting))

        val loadedPainting = paintingDao.getPaintingById(1)
        assertEquals(painting.title, loadedPainting?.title)
        assertEquals(painting.year, loadedPainting?.year)
        assertEquals(painting.history, loadedPainting?.history)
        assertEquals(painting.style, loadedPainting?.style)
        assertEquals(painting.image, loadedPainting?.image)
        assertEquals(painting.authorId, loadedPainting?.authorId)
    }

    @Test
    @Throws(Exception::class)
    fun insertAndReadAuthor() {
        val author = Author(2, "Винсент Ван Гог", 1853, "Художник постимпрессионизма")
        authorDao.insertAll(listOf(author))

        val loadedAuthor = authorDao.getAuthorById(2)
        assertEquals(author.name, loadedAuthor?.name)
        assertEquals(author.birthYear, loadedAuthor?.birthYear)
        assertEquals(author.authorFact, loadedAuthor?.authorFact)
    }

    @Test
    @Throws(Exception::class)
    fun testGetRandomAuthors() {
        val authors = listOf(
            Author(1, "Автор 1", 1900, "Факт об авторе 1"),
            Author(2, "Автор 2", 1900, "Факт об авторе 2"),
            Author(3, "Автор 3", 1900, "Факт об авторе 3")
        )
        authorDao.insertAll(authors)

        val randomAuthors = authorDao.getRandomAuthors(2)
        assertEquals(2, randomAuthors.size)
        assertTrue(randomAuthors.all { it.id in listOf(1, 2, 3) })
    }

    @Test
    @Throws(Exception::class)
    fun testGetRandomPaintings() {
        val author = Author(1, "Автор", 1900, "Факт об авторе")
        authorDao.insertAll(listOf(author))

        val paintings = listOf(
            Painting(1, "Картина 1", 1900, "История 1", "Стиль 1", "Изображение 1", 1),
            Painting(2, "Картина 2", 1900, "История 2", "Стиль 2", "Изображение 2", 1),
            Painting(3, "Картина 3", 1900, "История 3", "Стиль 3", "Изображение 3", 1)
        )
        paintingDao.insertAll(paintings)

        val randomPaintings = paintingDao.getRandomPaintings(2)
        assertEquals(2, randomPaintings.size)
        assertTrue(randomPaintings.all { it.id in listOf(1, 2, 3) })
    }

    @Test
    @Throws(Exception::class)
    fun testGetPaintingHistoryById() {
        val author = Author(1, "Автор", 1900, "Факт об авторе")
        authorDao.insertAll(listOf(author))

        val painting = Painting(1, "Картина", 1900, "История картины", "Стиль", "Изображение", 1)
        paintingDao.insertAll(listOf(painting))

        val paintingHistory = paintingDao.getPaintingHistoryById(1)
        assertEquals("История картины", paintingHistory)
    }

    @Test
    @Throws(Exception::class)
    fun testGetAuthorsWithPaintings() {
        val authors = listOf(
            Author(1, "Автор 1", 1900, "Факт об авторе 1"),
            Author(2, "Автор 2", 1900, "Факт об авторе 2")
        )
        authorDao.insertAll(authors)

        val paintings = listOf(
            Painting(1, "Картина 1", 1900, "История 1", "Стиль 1", "Изображение 1", 1),
            Painting(2, "Картина 2", 1900, "История 2", "Стиль 2", "Изображение 2", 2)
        )
        paintingDao.insertAll(paintings)

        val authorWithPaintingsList = authorDao.getAuthorsWithPaintings()
        assertEquals(2, authorWithPaintingsList.size)

        val author1WithPaintings = authorWithPaintingsList.first { it.author.id == 1 }
        assertEquals(1, author1WithPaintings.paintings.size)
        assertEquals(1, author1WithPaintings.paintings.first().id)

        val author2WithPaintings = authorWithPaintingsList.first { it.author.id == 2 }
        assertEquals(1, author2WithPaintings.paintings.size)
        assertEquals(2, author2WithPaintings.paintings.first().id)
    }

}
