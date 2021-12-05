package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.parcelize.Parcelize
import java.io.ByteArrayOutputStream
import kotlin.math.roundToInt

/**
 * Attribute that represents bitmap image.
 * @property bitmap Bitmap image.
 */
@Parcelize
data class BitmapAttribute(
    override val name: String,
    val bitmap: Bitmap
) : Attribute {

    companion object {

        private const val MAX_BITMAP_SIZE = 300.0f
        const val PNG_QUALITY = 70

        fun bitmap(
            name: String,
            bitmap: Bitmap,
            previewFormat: Bitmap.CompressFormat,
            previewQuality: Int,
        ): BitmapAttribute {
            val w = bitmap.width
            val h = bitmap.height
            val max = maxOf(w, h)
            val maxScaleRatio = MAX_BITMAP_SIZE / max
            val scaledBitmap = if (maxScaleRatio < 1.0f) {
                Bitmap.createScaledBitmap(bitmap, (w * maxScaleRatio).roundToInt(), (h * maxScaleRatio).roundToInt(), true)
            } else {
                bitmap
            }
            val byteArray = ByteArrayOutputStream().use { stream ->
                scaledBitmap.compress(previewFormat, previewQuality, stream)
                if (bitmap != scaledBitmap) {
                    scaledBitmap.recycle()
                }
                stream.toByteArray()
            }
            return BitmapAttribute(name, BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size))
        }
    }
}