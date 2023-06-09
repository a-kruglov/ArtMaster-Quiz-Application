package com.example.lvappquiz.ai_generation
import ArtObjectCallback
import Record
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.lvappquiz.R
import kotlinx.coroutines.launch
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.widget.ProgressBar
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.math.max
import kotlin.math.min


class AIGuessFragment : Fragment() {
    private lateinit var images: Array<ImageView>
    private var imageBitmaps = mutableListOf<Bitmap>()

    private lateinit var loadingSpinner: ProgressBar
    private lateinit var loadingText: TextView
    private lateinit var overlay: View

    var currentLoadingPercent = 0
    var rightAnswer = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_ai_guess, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingSpinner = view.findViewById(R.id.loading_spinner)
        loadingText = view.findViewById(R.id.loading_text)
        overlay = view.findViewById(R.id.loading_overlay)

        images = initImageViews(resources.obtainTypedArray(R.array.ai_images_ids), view)

        for (i in images.indices) {
            images[i].setOnClickListener { onAnswerClick(i) }
        }

        lifecycleScope.launch {
            loadImages()
        }
    }

    private fun onAnswerClick(i: Int) {
        if (rightAnswer == i) {
            lifecycleScope.launch {
                loadImages()
            }
        }
    }

    private fun initImageViews(answerImageIds: TypedArray, view: View): Array<ImageView> {
        val tempImages = Array(answerImageIds.length()) { i ->
            view.findViewById(answerImageIds.getResourceId(i, -1)) as ImageView
        }
        answerImageIds.recycle()
        return tempImages
    }

    private suspend fun loadImages() {
        Log.d("AIGuessFragment", "loadImages started")

        showLoading("Starting to load images...", 1)
        clear()

        try {
            HarvardArtMuseumsAPI.getRandomObjectInfo(object : ArtObjectCallback {
                override fun onSuccess(record: Record) {
                    lifecycleScope.launch { onRecordLoaded(record) }
                }

                override fun onFailure(e: Exception) {
                    Log.d("AIGuessFragment", "Error: ${e.message}")
                }
            })

        } catch (e: Exception) {
            Log.d("AIGuessFragment", "Error: ${e.message}")
        }
    }

    private suspend fun onRecordLoaded(record: Record) {
        showLoading("Loading the source image...", 2)
        val sourceImageBitmap = loadImageBitmapByUrl(record.primaryimageurl)
        if (sourceImageBitmap == null) {
            Log.d("AIGuessFragment", "Error: sourceImageBitmap is null")
            return
        }
        imageBitmaps.add(sourceImageBitmap)
        val minSize = min(sourceImageBitmap.width, sourceImageBitmap.height)
        val squaredSourceImage = ImageHelper.resizeImage(sourceImageBitmap, minSize, minSize)
        val byteArray = bitmapToByteArray(squaredSourceImage)

        loadGeneratedImageIntoView(sourceImageBitmap, images[0])
        loadGeneratedImageIntoView(squaredSourceImage, images[1])

        val imageUrls = withContext(Dispatchers.IO) {
            DalleAPI.generateImageVariations(byteArray, images.size - 1)
        }

        for (i in 1 until images.size) {
            processImageVariation(i, imageUrls[i - 1], sourceImageBitmap, minSize.toFloat())
        }
    }

    private suspend fun processImageVariation(
        i: Int,
        imageUrl: String,
        sourceImageBitmap: Bitmap,
        minSize: Float
    ) = withContext(Dispatchers.IO) {
        proceedPercentOfLoadingVariations()
        val variationBitmap = loadImageBitmapByUrl(imageUrl) ?: return@withContext
        val squareSize = calculateSquareSize(variationBitmap, sourceImageBitmap, minSize)
        val scaledBitmap = ImageHelper.resizeImage(variationBitmap, squareSize, squareSize)
        val bytes = bitmapToByteArray(scaledBitmap)
        val editedImageUrl = DalleAPI.editImage(bytes, 1)
        proceedPercentOfLoadingVariations()
        val editedBitmap = loadImageBitmapByUrl(editedImageUrl.first()) ?: return@withContext
        val (newWidth, newHeight) = calculateNewWidthAndHeight(sourceImageBitmap, editedBitmap)
        val finalBitmap = ImageHelper.resizeImage(editedBitmap, newWidth.toInt(), newHeight.toInt())

        withContext(Dispatchers.Main) {
            loadGeneratedImageIntoView(finalBitmap, images[i])
            imageBitmaps.add(finalBitmap)

            if (imageBitmaps.size == images.size) {
                shuffleAndSetImages()
            }
        }
    }

    private suspend fun proceedPercentOfLoadingVariations() {
        withContext(Dispatchers.Main) {
            currentLoadingPercent += 12
            showLoading("Generating image variations ${currentLoadingPercent}%...", 3)
        }
    }

    private suspend fun loadImageBitmapByUrl(imageUrl: String?): Bitmap? {
        return withContext(Dispatchers.IO) {
            Glide.with(this@AIGuessFragment)
                .asBitmap()
                .load(imageUrl)
                .submit()
                .get()
        }
    }

    private fun loadGeneratedImageIntoView(bitmap: Bitmap, imageView: ImageView) {
        Glide.with(this)
            .load(bitmap)
            .into(imageView)
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private suspend fun shuffleAndSetImages() {
        showLoading("Shuffle and set images", 4)
        val source = imageBitmaps[0]
        imageBitmaps.shuffle()

        for (i in images.indices) {
            loadGeneratedImageIntoView(imageBitmaps[i], images[i])

            if (imageBitmaps[i] == source) {
                rightAnswer = i
                Log.d("AIGuessFragment", "Source image is now at index $i")
            }
        }
        Log.d("AIGuessFragment", "loadImages ended")
        hideLoading()
    }

    private fun calculateSquareSize(
        variationBitmap: Bitmap,
        sourceImageBitmap: Bitmap,
        minSize: Float
    ): Int {
        val scale = max(sourceImageBitmap.width, sourceImageBitmap.height).toFloat() / minSize
        return min(variationBitmap.width * scale, variationBitmap.height * scale).toInt()
    }

    private fun calculateNewWidthAndHeight(
        sourceImageBitmap: Bitmap,
        editedBitmap: Bitmap
    ): Pair<Float, Float> {
        var newWidth = editedBitmap.width.toFloat()
        var newHeight = editedBitmap.height.toFloat()
        if (sourceImageBitmap.width > sourceImageBitmap.height) {
            newHeight =
                editedBitmap.height * (sourceImageBitmap.height / sourceImageBitmap.width.toFloat())
        } else {
            newWidth =
                editedBitmap.width * (sourceImageBitmap.width / sourceImageBitmap.height.toFloat())
        }
        return Pair(newWidth, newHeight)
    }

    private suspend fun showLoading(message: String, step: Int? = null) {
        withContext(Dispatchers.Main) {
            when (step) {
                1 -> loadingText.text =
                    "$message \n(Step 1 of 4: Fetching the artwork details from the Harvard Art Museums API)"

                2 -> loadingText.text =
                    "$message \n(Step 2 of 4: Loading and preparing the source image)"

                3 -> loadingText.text =
                    "$message \n(Step 3 of 4: Generating image variations with DALL·E)"

                4 -> loadingText.text =
                    "$message \n(Step 4 of 4: Loading and displaying the generated images)"

                else -> loadingText.text = message  // Default case, if no step number is provided
            }

            overlay.visibility = View.VISIBLE
        }
    }

    private suspend fun hideLoading() {
        withContext(Dispatchers.Main) {
            overlay.visibility = View.GONE
        }
    }

    private fun clear() {
        imageBitmaps.clear()
        currentLoadingPercent = 0
        rightAnswer = -1
    }
}
