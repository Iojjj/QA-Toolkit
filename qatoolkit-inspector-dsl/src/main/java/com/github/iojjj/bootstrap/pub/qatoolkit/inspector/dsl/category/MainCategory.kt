@file:Suppress("FunctionName")

package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category

import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_MAIN
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CATEGORY_NAME_MAIN
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder

/**
 * Builder that allows to add attributes to main attributes category.
 * @param allowNulls `true` if nullable attributes should be displayed in list of attributes, `false` to filter them out.
 * @param block builder block
 */
fun <T> InspectorBuilder<T>.MainCategory(
    allowNulls: Boolean = this.allowNulls,
    block: CategoryInspectorBuilder<T>.() -> Unit
) {
    addCategory(CATEGORY_NAME_MAIN, CATEGORY_ORDER_MAIN, allowNulls, block)
}