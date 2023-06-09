package com.example.lvappquiz.ai_generation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import kotlin.math.min

object ImageHelper {

    fun resizeImage(image: Bitmap, width: Int, height: Int): Bitmap {
        val resizedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val paint = Paint().apply {
            isAntiAlias = true
            isFilterBitmap = true
            isDither = true
        }
        val canvas = Canvas(resizedBitmap)
        val left = (width - image.width) / 2f
        val top = (height - image.height) / 2f
        val destRect = RectF(left, top, left + image.width, top + image.height)
        canvas.drawBitmap(image, null, destRect, paint)
        return resizedBitmap
    }
}
