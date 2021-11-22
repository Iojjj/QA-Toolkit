package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute

import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder

/**
 * Builder that allows to remove attribute.
 * @param name attribute name
 */
fun <T> CategoryInspectorBuilder<T>.Remove(
    name: String,
) {
    FlatMap(name) { emptyList() }
}