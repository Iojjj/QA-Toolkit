package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute

import android.graphics.Bitmap
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.BitmapAttribute.Companion.PNG_QUALITY
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.BitmapAttribute.Companion.bitmap
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder

/**
 * Builder that allows to add bitmap attribute.
 * @param name attribute name
 * @param order attribute order
 * @param previewFormat image preview format
 * @param previewQuality image preview quality
 * @param allowNulls `true` if nullable attribute should be displayed in list of attributes, `false` to filter them out.
 * @param bitmap bitmap attribute extractor
 */
fun <T> CategoryInspectorBuilder<T>.Bitmap(
    name: String,
    order: Int = this.incrementAndGetAttributeOrder(),
    previewFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
    previewQuality: Int = PNG_QUALITY,
    allowNulls: Boolean = this.allowNulls,
    bitmap: (T) -> Bitmap?,
) {
    add(name, order, allowNulls, bitmap) {
        listOf(
            bitmap(name, it, previewFormat, previewQuality)
        )
    }
}
