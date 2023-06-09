import android.os.Looper
import com.example.lvappquiz.BuildConfig
import com.squareup.moshi.JsonClass
import okhttp3.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException

@JsonClass(generateAdapter = true)
data class ArtObjectResponse(
    val info: Info,
    val records: List<Record>
)

@JsonClass(generateAdapter = true)
data class Info(
    val totalrecordsperquery: Int,
    val totalrecords: Int,
    val pages: Int,
    val page: Int,
    val next: String,
    val responsetime: String
)

@JsonClass(generateAdapter = true)
data class Record(
    val people: List<People>?,
    val primaryimageurl: String?,
    val dated: String?,
    val description: String?,
    val title: String?,
    val provenance: String?
)

@JsonClass(generateAdapter = true)
data class People(
    val name: String,
    val role: String,
    val culture: String?,
    val displaydate: String?
)

interface ArtObjectCallback {
    fun onSuccess(record: Record)
    fun onFailure(e: Exception)
}

object HarvardArtMuseumsAPI {
    private const val HARVARD_API_KEY = BuildConfig.HARVARD_API_KEY

    val client = OkHttpClient()
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    val jsonAdapter = moshi.adapter(ArtObjectResponse::class.java)

    val request = Request.Builder()
        .url("https://api.harvardartmuseums.org/object?apikey=${HARVARD_API_KEY}&q=_exists_:primaryimageurl&classification=Paintings&fields=primaryimageurl&size=100")
        .build()

    var pagesAmount: Int? = null

    fun init(){
        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                //callback.onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val responseBody = response.body?.string()
                    val artObjects = responseBody?.let { it1 -> jsonAdapter.fromJson(it1) }
                    pagesAmount = artObjects?.info?.pages
                }
            }
        })
    }

    fun getRandomObjectInfo(callback: ArtObjectCallback) {
        var randomPage = 1
        if (pagesAmount != null) {
            randomPage = (1..pagesAmount!!).random()
        }
        val requestFinal = Request.Builder().url("${request.url}&page=${randomPage}").build()
        client.newCall(requestFinal).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                android.os.Handler(Looper.getMainLooper()).post {
                    callback.onFailure(e)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val responseBody = response.body?.string()
                    val artObjects = responseBody?.let { it1 -> jsonAdapter.fromJson(it1) }
                    val records = artObjects?.records?.filter { it.primaryimageurl != null }
                    if (!records.isNullOrEmpty()) {
                        val randomRecord = records.random()
                        android.os.Handler(Looper.getMainLooper()).post {
                            callback.onSuccess(randomRecord)
                        }
                    } else {
                        android.os.Handler(Looper.getMainLooper()).post {
                            callback.onFailure(IOException("No records with non-null primaryimageurl"))
                        }
                    }
                }
            }
        })
    }
}



