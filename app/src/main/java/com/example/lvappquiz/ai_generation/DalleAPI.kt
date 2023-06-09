package com.example.lvappquiz.ai_generation
import com.example.lvappquiz.BuildConfig
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okio.BufferedSink
import java.io.IOException
import com.google.gson.annotations.SerializedName
import java.util.concurrent.TimeUnit

data class ImageVariationsResponse(
    @SerializedName("created") val created: Long,
    @SerializedName("data") val data: List<ImageData>
)

data class ImageData(
    @SerializedName("url") val url: String
)


object DalleAPI {
    private const val OPEN_AI_KEY = BuildConfig.OPEN_AI_KEY
    private const val VARIATIONS_BASE_URL = "https://api.openai.com/v1/images/variations"
    private const val EDIT_BASE_URL = "https://api.openai.com/v1/images/edits"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    fun generateImageVariations(byteArray : ByteArray, amount: Int): List<String> {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", "image.png",
                object : RequestBody() {
                    override fun contentType() = "image/png".toMediaType()

                    @Throws(IOException::class)
                    override fun writeTo(sink: BufferedSink) {
                        sink.write(byteArray)
                    }
                })
            .addFormDataPart("n", amount.toString())
            .addFormDataPart("size", "1024x1024")
            .build()

        val request = Request.Builder()
            .url(VARIATIONS_BASE_URL)
            .post(requestBody)
            .addHeader("Authorization", "Bearer $OPEN_AI_KEY")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            return response.body?.string()
                ?.let { parseJsonToGetImageUrls(it) }
                ?: emptyList()
        }
    }

    fun editImage(byteArray: ByteArray, n: Int, mask: ByteArray? = null): List<String> {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", "image.png",
                object : RequestBody() {
                    override fun contentType() = "image/png".toMediaType()

                    @Throws(IOException::class)
                    override fun writeTo(sink: BufferedSink) {
                        sink.write(byteArray)
                    }
                })
            .addFormDataPart("prompt", "item in the Harvard Art Museums collection")
            .addFormDataPart("n", n.toString())
            .addFormDataPart("size", "1024x1024")

        mask?.let {
            requestBody.addFormDataPart("mask", "mask.png",
                object : RequestBody() {
                    override fun contentType() = "image/png".toMediaType()

                    @Throws(IOException::class)
                    override fun writeTo(sink: BufferedSink) {
                        sink.write(it)
                    }
                })
        }

        val request = Request.Builder()
            .url(EDIT_BASE_URL)
            .post(requestBody.build())
            .addHeader("Authorization", "Bearer $OPEN_AI_KEY")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            return response.body?.string()
                ?.let { parseJsonToGetImageUrls(it) }
                ?: emptyList()
        }
    }

    private fun parseJsonToGetImageUrls(jsonString: String): List<String> {
        val gson = Gson()
        val response = gson.fromJson(jsonString, ImageVariationsResponse::class.java)
        return response.data.map { it.url }
    }
}
