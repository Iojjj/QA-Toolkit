package com.github.iojjj.bootstrap.qatoolkit.pool

interface Pool<T> {

    fun acquire(): T
    fun release(obj: T)
}

inline fun <T, R> Pool<T>.using(block: (T) -> R): R {
    val obj = acquire()
    val result = block(obj)
    release(obj)
    return result
}