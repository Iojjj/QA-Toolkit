package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.DrawableWrapper
import android.graphics.drawable.NinePatchDrawable
import android.graphics.drawable.ScaleDrawable
import android.os.Build
import androidx.core.graphics.drawable.toBitmap
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.Attribute
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.BitmapAttribute.Companion.bitmap
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.string

internal fun bitmapDrawableAttributes(
    name: String,
    drawable: Drawable,
    tint: ColorStateList?,
    previewFormat: Bitmap.CompressFormat,
    previewQuality: Int,
): Sequence<Attribute> {
    return sequenceOf(
        string("$name: Type", drawable::class.qualifiedName!!),
        try {
            preview("$name: Preview", drawable, tint, previewFormat, previewQuality)
        } catch (e: Exception) {
            string("$name: Preview", "Unable to create a preview: ${e.message}.")
        },
    ).filterNotNull()
}

private fun preview(
    name: String,
    drawable: Drawable,
    tint: ColorStateList?,
    previewFormat: Bitmap.CompressFormat,
    previewQuality: Int,
): Attribute {
    val wrappedDrawable = when {
        false && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && drawable is DrawableWrapper -> {
            drawable.drawable
        }
        false && drawable is ScaleDrawable -> {
            drawable.drawable
        }
        else -> {
            null
        }
    }?.mutate()?.apply {
        state = drawable.state
    }
    return if (wrappedDrawable != null) {
        preview(name, wrappedDrawable, null, previewFormat, previewQuality)
    } else {
        drawable.mutate().let { mutatedDrawable ->
            val oldColorFilter = mutatedDrawable.colorFilter
            if (oldColorFilter != null) {
                mutatedDrawable.colorFilter = null
            }
            if (tint != null) {
                mutatedDrawable.setTintList(null)
            }
            val w: Int
            val h: Int
            if (mutatedDrawable.intrinsicWidth <= 0 || mutatedDrawable.intrinsicHeight <= 0) {
                if (mutatedDrawable.bounds.isEmpty) {
                    w = 100
                    h = 100
                } else {
                    w = mutatedDrawable.bounds.width()
                    h = mutatedDrawable.bounds.height()
                }
            } else if (mutatedDrawable is NinePatchDrawable) {
                // Instantiate paint object
                mutatedDrawable.paint
                w = 100
                h = 100
            } else {
                w = mutatedDrawable.intrinsicWidth
                h = mutatedDrawable.intrinsicHeight
            }
            var bitmap: Bitmap? = null
            try {
                bitmap = mutatedDrawable.toBitmap(w, h)
                bitmap(name, bitmap, previewFormat, previewQuality)
            } finally {
                if (oldColorFilter != null) {
                    mutatedDrawable.colorFilter = oldColorFilter
                }
                if (tint != null) {
                    mutatedDrawable.setTintList(tint)
                }
                if (bitmap != null && mutatedDrawable is BitmapDrawable && bitmap != mutatedDrawable.bitmap) {
                    bitmap.recycle()
                }
            }
        }
    }
}