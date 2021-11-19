package com.github.iojjj.bootstrap.qatoolkit.pool

class SimplePool<T : Any>(
    private val factory: () -> T,
    private val identity: T.() -> Int = Any::hashCode
) : Pool<T> {

    internal val available = ArrayDeque<T>()
    internal val inUse = mutableMapOf<Int, T>()

    override fun acquire(): T {
        val obj = available.removeLastOrNull()
            ?: factory()
        inUse[obj.identity()] = obj
        return obj
    }

    override fun release(obj: T) {
        if (obj is MutableCollection<*>) {
            obj.clear()
        }
        inUse.remove(obj.identity())
        available.addLast(obj)
    }
}