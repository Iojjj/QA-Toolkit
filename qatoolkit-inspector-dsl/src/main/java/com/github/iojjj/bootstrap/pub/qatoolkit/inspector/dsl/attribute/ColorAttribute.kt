package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute

import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ColorAttribute.Companion.color
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder

/**
 * Builder that allows to add color attribute.
 * @param name attribute name
 * @param order attribute order
 * @param allowNulls `true` if nullable attribute should be displayed in list of attributes, `false` to filter them out.
 * @param color color attribute extractor
 */
fun <T> CategoryInspectorBuilder<T>.Color(
    name: String,
    order: Int = this.incrementAndGetAttributeOrder(),
    allowNulls: Boolean = this.allowNulls,
    color: (T) -> Int?
) {
    add(name, order, allowNulls, color) {
        listOf(color(name, it))
    }
}