package com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.service

import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import kotlin.reflect.KClass
import kotlin.reflect.cast

class ServiceToClientMessenger<T : Any>(
    messageType: KClass<T>,
    messageHandler: (ClientMessage<T>) -> Any
) {

    private val messenger by lazy {
        Messenger(MessagesHandler(messageType, messageHandler))
    }

    val binder: IBinder?
        get() = messenger.binder

    private class MessagesHandler<T : Any>(
        private val messageType: KClass<T>,
        private val handleClientMessage: (ClientMessage<T>) -> Any
    ) : Handler(Looper.getMainLooper()) {

        override fun handleMessage(msg: Message) {
            ServiceRequest.fromMessage(msg).fold(
                onSuccess = { request ->
                    when {
                        messageType.isInstance(request) -> {
                            handleClientMessage(ClientMessage.Received(messageType.cast(request)) {
                                msg.replyTo.send(ServiceResponse.toMessage(it))
                            })
                        }
                        request == null -> {
                            throw IllegalStateException()
                        }
                        else -> {
                            handleClientMessage(ClientMessage.UnsupportedType(request))
                        }
                    }
                },
                onFailure = {
                    handleClientMessage(ClientMessage.Unknown(msg))
                }
            )
        }
    }
}