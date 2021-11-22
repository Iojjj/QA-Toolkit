package com.github.iojjj.bootstrap.pub.qatoolkit.inspector

import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.Attribute

/**
 * Wrapper over delegate that lazily inspects object and returns list of object attributes.
 * @param T Type of object.
 * @property name Attributes category name.
 * @property order Attributes category sort order.
 * @property delegate Delegate that lazily inspects object.
 * @constructor
 */
class AttributeInspector<T>(
    override val name: String,
    override val order: Int,
    internal val delegate: AttributeInspector<T>.(T) -> Iterable<Attribute>,
) : Sortable {

    fun inspect(view: T): Iterable<Attribute> {
        return delegate(view)
    }
}