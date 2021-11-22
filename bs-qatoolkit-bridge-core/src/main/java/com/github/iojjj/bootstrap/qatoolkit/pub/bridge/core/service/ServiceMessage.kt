package com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.service

import android.os.Message

@Suppress("unchecked_cast")
sealed class ServiceMessage<R> {

    data class Received<R>(val data: R) : ServiceMessage<R>()

    data class UnsupportedType<T>(
        val response: Any,
    ) : ServiceMessage<T>()

    data class Unknown<T>(
        val message: Message,
    ) : ServiceMessage<T>()

    class ConnectionFailure<R> private constructor() : ServiceMessage<R>() {
        companion object {
            private val INSTANCE = ConnectionFailure<Unit>()
            operator fun <T> invoke(): ConnectionFailure<T> {
                return INSTANCE as ConnectionFailure<T>
            }
        }
    }

    class PermissionRequired<R> private constructor() : ServiceMessage<R>() {
        companion object {
            private val INSTANCE = PermissionRequired<Unit>()
            operator fun <T> invoke(): PermissionRequired<T> {
                return INSTANCE as PermissionRequired<T>
            }
        }
    }
}