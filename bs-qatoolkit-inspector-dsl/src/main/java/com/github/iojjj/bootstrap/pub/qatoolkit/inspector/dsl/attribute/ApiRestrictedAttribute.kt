@file:Suppress("FunctionName")

package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.attribute

import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.apiRestricted
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder

/**
 * Builder that allows to add information that attribute can't be retrieved because of API restrictions.
 * @param name attribute name
 * @param order attribute order
 * @param minSdk minimum SDK version required to retrieve an attribute
 */
fun <T> CategoryInspectorBuilder<T>.ApiRestricted(
    name: String,
    minSdk: Int,
    order: Int = this.incrementAndGetAttributeOrder(),
) {
    add(name, order) {
        listOf(apiRestricted(name, minSdk))
    }
}

/**
 * Builder that allows to add information that attribute can't be retrieved because of API restrictions.
 * @param name attribute name
 * @param order attribute order
 * @param minSdk minimum SDK version required to retrieve an attribute
 * @param dependsOn block that returns `null` if attribute can't be retrieved because of API restrictions
 */
fun <T> CategoryInspectorBuilder<T>.ApiRestricted(
    name: String,
    minSdk: Int,
    order: Int = this.incrementAndGetAttributeOrder(),
    dependsOn: (T) -> Any?,
) {
    add(name, order, allowNulls = false, dependsOn) {
        listOf(apiRestricted(name, minSdk))
    }
}