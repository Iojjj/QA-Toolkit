package com.github.iojjj.bootstrap.pub.qatoolkit.inspector

import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CATEGORY_NAME_MAIN
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CATEGORY_VIEW_LAYOUT
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CATEGORY_VIEW_STATE

/**
 * Collection of [AttributeInspector] merged into single category.
 * @param T Type of inspectable object.
 * @property name Category name.
 * @property order Category index.
 * @property attributeInspectors Collection of single attribute inspector.
 * @constructor
 */
class CategoryInspector<T>(
    override val name: String,
    override val order: Int,
    val attributeInspectors: Iterable<AttributeInspector<T>>
) : Sortable

fun <T> mergeCategories(vararg categories: Sequence<CategoryInspector<T>>): Sequence<CategoryInspector<T>> {
    return categories.asSequence()
        .flatten()
        .groupBy { it.name }
        .asSequence()
        .map { (name, categories) ->
            mergeCategories(name, categories)
        }
        .sorted()
}

private fun <T> mergeCategories(
    name: String,
    categories: List<CategoryInspector<T>>
): CategoryInspector<T> {
    val newCategoryOrder = when (name) {
        CATEGORY_NAME_MAIN -> CATEGORY_ORDER_MAIN
        CATEGORY_NAME_UNCATEGORIZED -> CATEGORY_ORDER_UNCATEGORIZED
        CATEGORY_VIEW_LAYOUT -> CATEGORY_ORDER_VIEW_LAYOUT
        CATEGORY_VIEW_BACKGROUND -> CATEGORY_ORDER_VIEW_BACKGROUND
        CATEGORY_VIEW_FOREGROUND -> CATEGORY_ORDER_VIEW_FOREGROUND
        CATEGORY_VIEW_TRANSFORM -> CATEGORY_ORDER_VIEW_TRANSFORM
        CATEGORY_VIEW_STATE -> CATEGORY_ORDER_VIEW_STATE
        else -> categories.minOf { it.order }
    }
    val sortedAttributes = categories.asSequence()
        // Get all attributes.
        .flatMap { it.attributeInspectors }
        // Remove duplicates.
        .groupBy { it.name }
        .map { (name, attributes) ->
            val newAttributeOrder = attributes.minOf { it.order }
            AttributeInspector(name, newAttributeOrder, attributes.last().delegate)
        }
        // Sort by order, then by name.
        .sorted()
    return CategoryInspector(name, newCategoryOrder, sortedAttributes)
}