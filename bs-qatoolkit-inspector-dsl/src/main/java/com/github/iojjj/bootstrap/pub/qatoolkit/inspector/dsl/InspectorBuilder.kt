package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl

import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_NAME_UNCATEGORIZED
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_UNCATEGORIZED
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_VIEW_LAYOUT
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CATEGORY_NAME_MAIN
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.mergeCategories

@AttributesCategoryDsl
class InspectorBuilder<T>(
    val allowNulls: Boolean,
    val order: Int,
) {

    private val categories = mutableListOf<CategoryInspector<T>>()

    internal fun checkCategoryParams(
        name: String,
        order: Int,
    ) {
        val builderName = InspectorBuilder::class.simpleName
        require(name != CATEGORY_NAME_MAIN) {
            "Category name \"$CATEGORY_NAME_MAIN\" is reserved. " +
                "Use $builderName.main() if you want to add attributes to main section."
        }
        require(name != CATEGORY_NAME_UNCATEGORIZED) {
            "Category name \"$CATEGORY_NAME_UNCATEGORIZED\" is reserved. " +
                "Use $builderName.uncategorized() if you want to add attributes to uncategorized section."
        }
        val categoryOrderRange = CATEGORY_ORDER_VIEW_LAYOUT + 1 until CATEGORY_ORDER_UNCATEGORIZED
        require(order in categoryOrderRange) {
            "Category order must be in range [${categoryOrderRange.first}, ${categoryOrderRange.last}]. " +
                "Use $builderName.main() or $builderName.uncategorized() " +
                "if you want to add attributes to the first or the last section accordingly."
        }
    }

    internal fun addCategory(
        name: String,
        order: Int = this.order,
        allowNulls: Boolean = this.allowNulls,
        block: CategoryInspectorBuilder<T>.() -> Unit
    ) {
        categories += CategoryInspectorBuilder<T>(name, order, allowNulls)
            .apply(block)
            .build()
    }

    internal fun build(): Iterable<CategoryInspector<T>> {
        return mergeCategories(categories.asSequence()).toList()
    }
}