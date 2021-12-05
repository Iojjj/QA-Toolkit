package com.github.iojjj.bootstrap.internal.qatoolkit.inspector.dsl.reflection

import kotlin.reflect.KClass

internal class ReflectionApi<T> private constructor(
    private val type: Class<T>,
) {

    private val fields = mutableMapOf<ReflectionField.Key<*>, Any>()

    fun <R> field(key: ReflectionField.Key<R>): ReflectionField<T, R>? {
        val field = fields.getOrPut(key) {
            try {
                ReflectionField<T, R>(type.getDeclaredField(key.name).apply {
                    isAccessible = true
                })
            } catch (e: NoSuchFieldException) {
                NO_SUCH_FIELD
            }
        }
        return field.takeUnless { it === NO_SUCH_FIELD }
            ?.let { it as ReflectionField<T, R> }
    }

    companion object {

        private val NO_SUCH_FIELD = Any()

        inline operator fun <reified T : Any> invoke(): ReflectionApi<T> {
            return invoke(T::class)
        }

        operator fun <T : Any> invoke(type: KClass<T>): ReflectionApi<T> {
            return ReflectionApi(type.java)
        }

        operator fun <T : Any> invoke(type: Class<T>): ReflectionApi<T> {
            return ReflectionApi(type)
        }

        inline operator fun invoke(className: String): ReflectionApi<Any>? {
            return try {
                invoke(Class.forName(className) as Class<Any>)
            } catch (e: ClassNotFoundException) {
                null
            }
        }
    }
}
