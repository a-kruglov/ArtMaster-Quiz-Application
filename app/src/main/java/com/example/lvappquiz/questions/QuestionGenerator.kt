package com.example.lvappquiz.questions
import android.content.Context
import com.bumptech.glide.Glide
import com.example.lvappquiz.db.AppDatabase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

class QuestionGenerator(private val questionType: QuestionType, private val context: Context) {

    private val database = AppDatabase.getInstance(context)
    private val questionFlow: MutableSharedFlow<QuestionData> = MutableSharedFlow(replay = 1)
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val replenishThreshold = 5

    private val operationMap: Map<QuestionType, () -> QuestionData> = mapOf(
        QuestionType.GuessBirthYearOfAuthor to ::generateGuessBirthYearOfAuthorQuestion,
        QuestionType.GuessAuthorByPicture to ::generateGuessAuthorByPictureQuestion,
        QuestionType.GuessYearByPicture to ::generateGuessYearByPictureQuestion,
        QuestionType.GuessPictureByAuthor to ::generateGuessPictureByAuthorQuestion,
        QuestionType.Random to ::generateRandomQuestion
    )
    init {
        coroutineScope.launch {
            replenishQueue()
        }
    }
    suspend fun getQuestion(): QuestionData {
        if (questionFlow.replayCache.size < replenishThreshold) {
            coroutineScope.launch {
                replenishQueue()
            }
        }
        return questionFlow.first()
    }
    private suspend fun replenishQueue() {
        val replenishAmount = 10

        for (i in 0 until replenishAmount) {
            val questionData = generateQuestion()
            questionFlow.emit(questionData)
        }
    }
    private fun generateQuestion(): QuestionData {
        return generateQuestionOfType(questionType)
    }
    private fun generateQuestionOfType(questionType: QuestionType): QuestionData {
        return operationMap[questionType]?.invoke()
            ?: throw IllegalStateException("Question type not found")
    }
    private fun generateRandomQuestion(): QuestionData {
        val concreteQuestionType =
            QuestionType.values().filter { it != QuestionType.Random }.random()
        return generateQuestionOfType(concreteQuestionType)
    }
    private fun generateGuessAuthorByPictureQuestion(): QuestionData {
        val painting = database.paintingDao().getRandomPaintings(1).first()
        val correctAuthorId = painting.authorId
        val incorrectAuthors = database.authorDao().getRandomAuthors(3, correctAuthorId)

        val correctAuthor = database.authorDao().getAuthorById(correctAuthorId)
            ?: throw IllegalStateException("Author not found")

        val answers = incorrectAuthors.map { it.name }.toMutableList()
            .apply { add(correctAuthor.name); shuffle() }

        val correctAnswerIndex = answers.indexOf(correctAuthor.name)

        preLoadImages(painting.image)

        return QuestionData(
            type = QuestionType.GuessAuthorByPicture,
            image = painting.image,
            questionText = "Кто написал эту картину?",
            answers = answers,
            correctAnswerIndex = correctAnswerIndex,
            correctAuthor.authorFact
        )
    }

    private fun generateGuessYearByPictureQuestion(): QuestionData {
        val painting = database.paintingDao().getRandomPaintings(1).first()
        val correctYear = painting.year

        val incorrectYears = database.paintingDao().getRandomYears(3, correctYear)

        val answers =
            incorrectYears.map { it }.toMutableList().apply { add(correctYear); shuffle() }

        val correctAnswerIndex = answers.indexOf(correctYear)
        preLoadImages(painting.image)

        return QuestionData(
            type = QuestionType.GuessYearByPicture,
            image = painting.image,
            questionText = "В каком году была написана картина?",
            answers = answers.map { it.toString() },
            correctAnswerIndex = correctAnswerIndex,
            painting.history
        )
    }
    private fun generateGuessPictureByAuthorQuestion(): QuestionData {
        val correctPainting = database.paintingDao().getRandomPaintings(1).first()
        val author = correctPainting.authorId
        val incorrectPaintings = database.paintingDao().getRandomPaintingsIgnoringAuthor(3, author)
        val answers = incorrectPaintings.map { it.image }.toMutableList()
            .apply { add(correctPainting.image); shuffle() }

        val correctAnswerIndex = answers.indexOf(correctPainting.image)
        val authorName = database.authorDao().getAuthorById(author)?.name
            ?: throw IllegalStateException("Author not found")
        preLoadImages(*answers.toTypedArray())

        return QuestionData(
            type = QuestionType.GuessPictureByAuthor,
            image = null,
            questionText = "Какую из этих картин написал ${authorName}?",
            answers = answers,
            correctAnswerIndex = correctAnswerIndex,
            correctPainting.history
        )
    }
    private fun generateGuessBirthYearOfAuthorQuestion(): QuestionData {
        val author = database.authorDao().getRandomAuthors(1).first()
        val correctYear = author.birthYear
        val incorrectYears = database.authorDao().getRandomBirthYears(3, correctYear)

        val answers = incorrectYears.map { it }.toMutableList()
            .apply { add(correctYear); shuffle() }

        val correctAnswerIndex = answers.indexOf(correctYear)

        return QuestionData(
            type = QuestionType.GuessBirthYearOfAuthor,
            image = null,
            questionText = "В каком году родился ${author.name}?",
            answers = answers.map { it.toString() },
            correctAnswerIndex = correctAnswerIndex,
            author.authorFact
        )
    }
    private fun preLoadImages(vararg urls: String) {
        urls.forEach { Glide.with(context).load(it).preload() }
    }
}
