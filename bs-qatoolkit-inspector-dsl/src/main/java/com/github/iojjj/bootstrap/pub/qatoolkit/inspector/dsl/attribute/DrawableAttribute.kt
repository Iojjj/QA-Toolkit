package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.Property
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.bitmapDrawableAttributes
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.colorDrawableAttributes
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.commonDrawableAttributes
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.gradientDrawableAttributes
import com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute.rippleDrawableAttributes
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.BitmapAttribute.Companion.PNG_QUALITY
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.apiRestricted
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder

/**
 * Builder that allows to add drawable attribute.
 * @param name attribute name
 * @param order attribute order
 * @param previewFormat image preview format
 * @param previewQuality image preview quality
 * @param allowNulls `true` if nullable attribute should be displayed in list of attributes, `false` to filter them out.
 * @param drawable drawable attribute extractor
 */
fun <T> CategoryInspectorBuilder<T>.Drawable(
    name: String,
    previewFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
    previewQuality: Int = PNG_QUALITY,
    order: Int = this.incrementAndGetAttributeOrder(),
    allowNulls: Boolean = this.allowNulls,
    drawable: (T) -> Drawable?,
) {
    TintedDrawable(name, previewFormat, previewQuality, order, allowNulls) {
        drawable(it) to null
    }
}

/**
 * Builder that allows to add tinted drawable attribute.
 * @param name attribute name
 * @param order attribute order
 * @param previewFormat image preview format
 * @param previewQuality image preview quality
 * @param allowNulls `true` if nullable attribute should be displayed in list of attributes, `false` to filter them out.
 * @param tintedDrawable tinted drawable attribute extractor
 */
fun <T> CategoryInspectorBuilder<T>.TintedDrawable(
    name: String,
    previewFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
    previewQuality: Int = PNG_QUALITY,
    order: Int = this.incrementAndGetAttributeOrder(),
    allowNulls: Boolean = this.allowNulls,
    tintedDrawable: (T) -> Pair<Drawable?, ColorStateList?>?,
) {
    val nullCheck: (T) -> Pair<Drawable, ColorStateList?>? = {
        val (drawable, tint) = tintedDrawable(it)
            ?: Pair(null, null)
        when (drawable) {
            null -> null
            else -> drawable to tint
        }
    }
    add(name, order, allowNulls, nullCheck) { (drawable, tint) ->
        when (drawable) {
            is ColorDrawable -> {
                sequenceOf(
                    colorDrawableAttributes(name, drawable),
                    commonDrawableAttributes(name, drawable, tint)
                )
            }
            is GradientDrawable -> {
                sequenceOf(
                    gradientDrawableAttributes(name, drawable),
                    commonDrawableAttributes(name, drawable, tint)
                )
            }
            is RippleDrawable -> {
                sequenceOf(
                    rippleDrawableAttributes(name, drawable),
                    commonDrawableAttributes(name, drawable, tint)
                )
            }
            else -> {
                sequenceOf(
                    bitmapDrawableAttributes(name, drawable, tint, previewFormat, previewQuality),
                    commonDrawableAttributes(name, drawable, tint)
                )
            }
        }.flatten().toList()
    }
}

internal fun <T, R> Property<T>.fold(
    onKnown: (T) -> R,
    onUnknown: (Int) -> R,
): R {
    return when (this) {
        is Property.Known -> onKnown(value)
        is Property.Unknown -> onUnknown(api)
    }
}

internal fun <T, R> Property<T>.fold(
    attribute: String,
    onUnknown: (attribute: String, api: Int) -> R = { name, api ->
        apiRestricted(name, api) as R
    },
    onKnown: (attribute: String, value: T) -> R,
): R {
    return fold(
        onKnown = { onKnown(attribute, it) },
        onUnknown = { onUnknown(attribute, it) }
    )
}

internal fun <T, R> Property<T>.foldSequence(
    name: String,
    onKnown: (String, T) -> Sequence<R>,
    onUnknown: (String, Int) -> Sequence<R>,
): Sequence<R> {
    return fold(
        onKnown = { onKnown(name, it) },
        onUnknown = { onUnknown(name, it) }
    )
}


