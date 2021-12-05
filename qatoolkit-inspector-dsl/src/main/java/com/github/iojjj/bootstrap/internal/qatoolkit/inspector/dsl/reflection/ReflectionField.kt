package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.reflection

import java.lang.reflect.Field

internal class ReflectionField<T, R>(
    private val field: Field
) {

    @Suppress("UNCHECKED_CAST")
    fun get(obj: T): R {
        return field.get(obj) as R
    }

    class Key<R>(
        val api: Int,
        val name: String
    )
}
