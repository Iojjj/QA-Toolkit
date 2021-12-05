package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute

import androidx.annotation.ColorInt
import kotlinx.parcelize.Parcelize

/**
 * Attribute that represents color.
 * @property color Color value.
 */
@Parcelize
data class ColorAttribute(
    override val name: String,
    val color: Int,
) : Attribute {

    companion object {

        fun color(
            name: String,
            @ColorInt
            color: Int
        ): ColorAttribute {
            return ColorAttribute(name, color)
        }
    }
}