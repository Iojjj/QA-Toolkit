package com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.service

import android.os.Message
import android.os.Parcelable

sealed class ClientMessage<T> {

    data class Received<T>(
        val request: T,
        internal val reply: (Parcelable) -> Unit
    ) : ClientMessage<T>()

    data class UnsupportedType<T>(
        val request: Any,
    ) : ClientMessage<T>()

    data class Unknown<T>(
        val message: Message,
    ) : ClientMessage<T>()
}

@Suppress("UNCHECKED_CAST")
fun <R : ServiceRequest<*>> ClientMessage.Received<*>.of(request: R): ClientMessage.Received<R> {
    return if (this.request === request) {
        this as ClientMessage.Received<R>
    } else {
        ClientMessage.Received(request, reply)
    }
}

fun <R : ServiceRequest<Response>, Response : Parcelable> ClientMessage.Received<R>.replyWith(response: Response) {
    reply(response)
}