package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.attribute

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import com.github.iojjj.bootstrap.pub.core.stdlib.FLOAT_ALPHA_MAX
import com.github.iojjj.bootstrap.pub.core.stdlib.INT_ALPHA_RANGE
import com.github.iojjj.bootstrap.pub.core.stdlib.toFloatAlpha
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.Attribute
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ColorAttribute.Companion.color
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.float
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.string
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.DimensionAttribute.Companion.dimension

internal fun commonDrawableAttributes(
    name: String,
    drawable: Drawable,
    tint: ColorStateList?,
): Sequence<Attribute> {
    return sequenceOf(
        tint?.let {
            color("$name: Tint", it.getColorForState(drawable.state, it.defaultColor))
        },
        float("$name: Alpha", drawable.alphaSafe),
        size("$name: Width", drawable.intrinsicWidth),
        size("$name: Height", drawable.intrinsicHeight),
    ).filterNotNull()
}

private fun size(
    name: String,
    value: Int
): Attribute {
    return if (value < 0) {
        string(name, "match_parent")
    } else {
        dimension(name, value.toFloat())
    }
}

private val Drawable.alphaSafe: Float
    get() = alpha.takeUnless { it !in INT_ALPHA_RANGE }
        ?.toFloatAlpha()
        ?: FLOAT_ALPHA_MAX