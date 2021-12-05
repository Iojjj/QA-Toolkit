package com.github.iojjj.bootstrap.pub.qatoolkit.inspector.dsl

import com.github.iojjj.bootstrap.pub.core.collections.getOrDefaultCompat
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.AttributeInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.CategoryInspector
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.Attribute
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.string

/**
 * Category builder.
 * @param T Type of inspectable object.
 * @property name Category name.
 * @property order Category order.
 * @property allowNulls Flag indicates if nullable attribute should be displayed in list of attributes.
 * @property attributesMap Attributes map where key is an attribute name.
 */
@AttributesCategoryDsl
class CategoryInspectorBuilder<T> internal constructor(
    private val name: String,
    private val order: Int,
    val allowNulls: Boolean,
) {

    private val attributesMap = mutableMapOf<String, AttributeInspector<T>>()

    internal fun build(): CategoryInspector<T> {
        return CategoryInspector(
            name = name,
            order = order,
            attributeInspectors = attributesMap.values.toList()
        )
    }

    internal fun incrementAndGetAttributeOrder(): Int {
        val order = ATTRIBUTE_ORDER_MAP.getOrDefaultCompat(name, 0)
        ATTRIBUTE_ORDER_MAP[name] = order + 1
        return order
    }

    internal fun add(
        name: String,
        order: Int,
        flatMap: AttributeInspector<T>.(T) -> Iterable<Attribute>
    ) {
        attributesMap[name] = AttributeInspector(name, order, flatMap)
    }

    internal fun <R> add(
        name: String,
        order: Int,
        allowNulls: Boolean,
        value: (T) -> R?,
        flatMap: AttributeInspector<T>.(R) -> Iterable<Attribute>
    ) {
        attributesMap[name] = AttributeInspector(name, order) { view ->
            val v = value(view)
            if (v == null && allowNulls) {
                listOf(string(name, "null"))
            } else if (v != null) {
                flatMap(v)
            } else {
                emptyList()
            }
        }
    }

    companion object {

        internal val ATTRIBUTE_ORDER_MAP = mutableMapOf<String, Int>()
    }
}