package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute

import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.AttributeInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.Attribute
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder

/**
 * Builder that allows to replace a single attribute with a collection.
 * @param name attribute name
 * @param block extractor of attributes collection
 */
fun <T> CategoryInspectorBuilder<T>.FlatMap(
    name: String,
    block: AttributeInspector<T>.(T) -> Iterable<Attribute>
) {
    add(name, this.incrementAndGetAttributeOrder(), block)
}