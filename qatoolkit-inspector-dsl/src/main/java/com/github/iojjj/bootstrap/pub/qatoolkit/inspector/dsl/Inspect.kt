@file:Suppress("FunctionName")

package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl

import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_DEFAULT
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_VIEW_BACKGROUND
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_VIEW_FOREGROUND
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_VIEW_LAYOUT
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_VIEW_STATE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_ORDER_VIEW_TRANSFORM
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_VIEW_BACKGROUND
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_VIEW_FOREGROUND
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CATEGORY_VIEW_TRANSFORM
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CATEGORY_VIEW_LAYOUT
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CATEGORY_VIEW_STATE

fun <T> Inspect(
    allowNulls: Boolean = false,
    defaultOrder: Int = CATEGORY_ORDER_DEFAULT,
    block: InspectorBuilder<T>.() -> Unit
): Iterable<CategoryInspector<T>> {
    return InspectorBuilder<T>(allowNulls, defaultOrder)
        .apply(block)
        .build()
}

fun <T> InspectorBuilder<T>.ViewLayoutCategory(
    allowNulls: Boolean = this.allowNulls,
    block: CategoryInspectorBuilder<T>.() -> Unit
) {
    addCategory(CATEGORY_VIEW_LAYOUT, CATEGORY_ORDER_VIEW_LAYOUT, allowNulls, block)
}

fun <T> InspectorBuilder<T>.ViewBackgroundCategory(
    allowNulls: Boolean = this.allowNulls,
    block: CategoryInspectorBuilder<T>.() -> Unit
) {
    addCategory(CATEGORY_VIEW_BACKGROUND, CATEGORY_ORDER_VIEW_BACKGROUND, allowNulls, block)
}

fun <T> InspectorBuilder<T>.ViewForegroundCategory(
    allowNulls: Boolean = this.allowNulls,
    block: CategoryInspectorBuilder<T>.() -> Unit
) {
    addCategory(CATEGORY_VIEW_FOREGROUND, CATEGORY_ORDER_VIEW_FOREGROUND, allowNulls, block)
}

fun <T> InspectorBuilder<T>.ViewTransformCategory(
    allowNulls: Boolean = this.allowNulls,
    block: CategoryInspectorBuilder<T>.() -> Unit
) {
    addCategory(CATEGORY_VIEW_TRANSFORM, CATEGORY_ORDER_VIEW_TRANSFORM, allowNulls, block)
}

fun <T> InspectorBuilder<T>.ViewStateCategory(
    allowNulls: Boolean = this.allowNulls,
    block: CategoryInspectorBuilder<T>.() -> Unit
) {
    addCategory(CATEGORY_VIEW_STATE, CATEGORY_ORDER_VIEW_STATE, allowNulls, block)
}