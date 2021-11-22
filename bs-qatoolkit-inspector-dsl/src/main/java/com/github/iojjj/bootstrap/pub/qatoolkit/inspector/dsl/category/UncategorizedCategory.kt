package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.category

import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_NAME_UNCATEGORIZED
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_UNCATEGORIZED
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.CategoryInspectorBuilder
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl.InspectorBuilder

/**
 * Builder that allows to add attributes to uncategorized attributes category.
 * @param allowNulls `true` if nullable attributes should be displayed in list of attributes, `false` to filter them out.
 * @param block builder block
 */
fun <T> InspectorBuilder<T>.Uncategorized(
    allowNulls: Boolean = this.allowNulls,
    block: CategoryInspectorBuilder<T>.() -> Unit
) {
    addCategory(CATEGORY_NAME_UNCATEGORIZED, CATEGORY_ORDER_UNCATEGORIZED, allowNulls, block)
}