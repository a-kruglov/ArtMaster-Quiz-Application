package com.example.lvappquiz.db

import android.content.Context
import com.example.lvappquiz.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.StringWriter


class DatabaseInitializer {
    fun insertData(context: Context) {
        val jsonString = loadJSONFromRaw(context, R.raw.painting)
        val jsonObject = JSONObject(jsonString)
        val jsonArray = jsonObject.getJSONArray("paintings")

        val authorList = mutableListOf<Author>()
        val paintingList = mutableListOf<Painting>()

        for (i in 0 until jsonArray.length()) {
            val paintingObject = jsonArray.getJSONObject(i)

            val authorObject = paintingObject.getJSONObject("author")
            val author = Author(
                authorObject.getInt("id"),
                authorObject.getString("name"),
                authorObject.getInt("birthYear"),
                authorObject.getString("authorFact")
            )
            authorList.add(author)

            val painting = Painting(
                paintingObject.getInt("id"),
                paintingObject.getString("title"),
                paintingObject.getInt("year"),
                paintingObject.getString("history"),
                paintingObject.getString("style"),
                paintingObject.getString("image"),
                author.id
            )
            paintingList.add(painting)
        }

        val appDatabase = AppDatabase.getInstance(context)
        appDatabase.runInTransaction {
            appDatabase.authorDao().insertAll(authorList)
            appDatabase.paintingDao().insertAll(paintingList)
        }
    }

    private fun loadJSONFromRaw(context: Context, resId: Int): String {
        val inputStream = context.resources.openRawResource(resId)
        val writer = StringWriter()
        val buffer = CharArray(1024)

        try {
            val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var n: Int = reader.read(buffer)
            while (n != -1) {
                writer.write(buffer, 0, n)
                n = reader.read(buffer)
            }
        } finally {
            inputStream.close()
        }
        return writer.toString()
    }
}

