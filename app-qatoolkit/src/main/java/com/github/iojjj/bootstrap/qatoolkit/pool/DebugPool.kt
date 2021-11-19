package com.github.iojjj.bootstrap.qatoolkit.pool

import android.util.Log

class DebugPool<T : Any>(
    private val delegate: SimplePool<T>
) : Pool<T> by delegate {

    private val nameMap = mutableMapOf<Int, String>()

    fun acquire(name: String): T {
        return acquire().also {
            Log.d("POOL", "Acquiring $name ${System.identityHashCode(it)}")
            nameMap[System.identityHashCode(it)] = name
            printState()
        }
    }

    override fun release(obj: T) {
        var exception: Exception? = null
        try {
            Log.d("POOL", "Releasing ${nameMap[System.identityHashCode(obj)]} ${System.identityHashCode(obj)}")
            delegate.release(obj)
        } catch (e: Exception) {
            exception = e
        } finally {
            printState()
            nameMap.remove(System.identityHashCode(obj))
            exception?.also {
                throw it
            }
        }
    }

    private fun printState() {
        val transform: (T) -> CharSequence = { "${System.identityHashCode(it)} -> ${nameMap[System.identityHashCode(it)]}" }
        Log.d("POOL", "Available (${delegate.available.size}):\n${delegate.available.joinToString("\n", transform = transform)}")
        Log.d("POOL", "In Use (${delegate.inUse.size}):\n${delegate.inUse.values.joinToString("\n", transform = transform)}")
        Log.d("POOL", "---------------------")
    }
}

inline fun <T : Any, R> DebugPool<T>.using(
    name: String,
    block: (T) -> R
): R {
    val obj = acquire(name)
    val result = block(obj)
    release(obj)
    return result
}