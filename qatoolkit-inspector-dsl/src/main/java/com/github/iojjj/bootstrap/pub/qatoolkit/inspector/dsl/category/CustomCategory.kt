package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category

import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder

/**
 * Builder that allows to add attributes to custom category.
 * @param name category name
 * @param order category order
 * @param allowNulls `true` if nullable attributes should be displayed in list of attributes, `false` to filter them out.
 * @param block builder block
 */
fun <T> InspectorBuilder<T>.Category(
    name: String,
    order: Int = this.order,
    allowNulls: Boolean = this.allowNulls,
    block: CategoryInspectorBuilder<T>.() -> Unit
) {
    checkCategoryParams(name, order)
    addCategory(name, order, allowNulls, block)
}