package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.DimensionAttribute.Companion.dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.DimensionAttribute.Companion.textDimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder

/**
 * Builder that allows to add dimension attribute (i.e. `16 dp`).
 * @param name attribute name
 * @param order attribute order
 * @param allowNulls `true` if nullable attribute should be displayed in list of attributes, `false` to filter them out.
 * @param dimension dimension attribute extractor
 */
fun <T> CategoryInspectorBuilder<T>.Dimension(
    name: String,
    order: Int = this.incrementAndGetAttributeOrder(),
    allowNulls: Boolean = this.allowNulls,
    dimension: (T) -> Number?,
) {
    add(name, order, allowNulls, dimension) {
        listOf(dimension(name, it))
    }
}

/**
 * Builder that allows to add text dimension attribute (i.e. `16 sp`).
 * @param name attribute name
 * @param order attribute order
 * @param allowNulls `true` if nullable attribute should be displayed in list of attributes, `false` to filter them out.
 * @param dimension text dimension attribute extractor
 */
fun <T> CategoryInspectorBuilder<T>.TextDimension(
    name: String,
    order: Int = this.incrementAndGetAttributeOrder(),
    allowNulls: Boolean = this.allowNulls,
    dimension: (T) -> Number?,
) {
    add(name, order, allowNulls, dimension) {
        listOf(textDimension(name, it))
    }
}

/**
 * Builder that allows to add layout dimension attribute (i.e. `match_parent` or `wrap_content`).
 * @param name attribute name
 * @param order attribute order
 * @param allowNulls `true` if nullable attribute should be displayed in list of attributes, `false` to filter them out.
 * @param dimension layout dimension attribute extractor
 */
fun <T> CategoryInspectorBuilder<T>.LayoutDimension(
    name: String,
    order: Int = this.incrementAndGetAttributeOrder(),
    allowNulls: Boolean = this.allowNulls,
    dimension: (T) -> Number?,
) {
    add(name, order, allowNulls, dimension) {
        listOf(
            when (it) {
                MATCH_PARENT -> {
                    CommonAttribute(name, "match_parent")
                }
                WRAP_CONTENT -> {
                    CommonAttribute(name, "wrap_content")
                }
                else -> {
                    dimension(name, it)
                }
            }
        )
    }
}