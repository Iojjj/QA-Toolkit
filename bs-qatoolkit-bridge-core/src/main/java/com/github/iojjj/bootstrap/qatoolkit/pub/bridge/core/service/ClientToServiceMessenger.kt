package com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.os.Parcelable
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.reflect.KClass
import kotlin.reflect.cast

class ClientToServiceMessenger(
    context: Context,
    val packageName: String,
    serviceClassName: String,
) {

    private val connection = ConnectionWithMessenger(context, packageName, serviceClassName)

    suspend inline fun <reified R : Parcelable> send(request: ServiceRequest<R>): ServiceMessage<R> {
        return send(R::class, request)
    }

    suspend fun <R : Parcelable> send(
        responseType: KClass<R>,
        message: ServiceRequest<R>
    ): ServiceMessage<R> {
        return when (val responseMessage = connection.send(ServiceRequest.toMessage(message))) {
            SendResult.NotConnected -> {
                ServiceMessage.ConnectionFailure()
            }
            SendResult.PermissionRequired -> {
                ServiceMessage.PermissionRequired()
            }
            is SendResult.Success -> {
                ServiceResponse.fromMessage(responseMessage.message).fold(
                    onSuccess = { response ->
                        when {
                            responseType.isInstance(response) -> {
                                ServiceMessage.Received(responseType.cast(response))
                            }
                            response == null -> {
                                throw IllegalStateException()
                            }
                            else -> {
                                ServiceMessage.UnsupportedType(response)
                            }
                        }
                    },
                    onFailure = {
                        ServiceMessage.Unknown(responseMessage.message)
                    }
                )
            }
        }
    }

    suspend fun connect(): ConnectionStatus {
        return connection.connect()
    }

    fun disconnect() {
        connection.disconnect()
    }

    private class ConnectionWithMessenger(
        private val context: Context,
        private val packageName: String,
        private val serviceClassName: String,
    ) : ServiceConnection {

        private val responseHandler = ResponseHandler()
        private val responseMessenger = Messenger(responseHandler)
        private var messenger: Messenger? = null
        private var bound = false

        var continuation: Continuation<ConnectionStatus>? = null

        override fun onServiceConnected(
            name: ComponentName,
            service: IBinder
        ) {
            messenger = Messenger(service)
            bound = true
            onConnectionStateChanged(ConnectionStatus.Connected)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            messenger = null
            bound = false
            onConnectionStateChanged(ConnectionStatus.NotConnected)
        }

        suspend fun send(message: Message): SendResult {
            message.replyTo = responseMessenger
            return when {
                bound -> {
                    sendOnBound(message)
                }
                else -> {
                    when (connect()) {
                        ConnectionStatus.Connected -> {
                            sendOnBound(message)
                        }
                        ConnectionStatus.NotConnected -> {
                            SendResult.NotConnected
                        }
                        ConnectionStatus.PermissionRequired -> {
                            SendResult.PermissionRequired
                        }
                    }
                }
            }
        }

        suspend fun connect(): ConnectionStatus {
            return suspendCoroutine { continuation ->
                this.continuation = continuation
                val intent = Intent().apply {
                    component = ComponentName(packageName, serviceClassName)
                }
                try {
                    if (!context.bindService(intent, this, Context.BIND_AUTO_CREATE)) {
                        // Can't bind to service by some reason.
                        onConnectionStateChanged(ConnectionStatus.NotConnected)
                    }
                } catch (e: SecurityException) {
                    // Don't have permission to connect to service.
                    onConnectionStateChanged(ConnectionStatus.PermissionRequired)
                }
            }
        }

        private suspend fun sendOnBound(message: Message): SendResult {
            return prepareForResponse {
                messenger?.send(message)
            }
                ?.let { SendResult.Success(it) }
                ?: SendResult.NotConnected
        }

        fun disconnect() {
            if (bound) {
                context.unbindService(this)
                bound = false
            }
        }

        private suspend fun prepareForResponse(block: () -> Unit): Message? {
            return suspendCoroutine { continuation ->
                responseHandler.continuation = continuation
                block()
            }
        }

        private fun onConnectionStateChanged(connectionStatus: ConnectionStatus) {
            // Replay to anyone waiting with empty message because a new connection has been established (or disconnected).
            // This must be done before resuming connection.
            responseHandler.onMessage(null)
            continuation?.resume(connectionStatus)
            continuation = null
        }
    }

    private class ResponseHandler : Handler(Looper.getMainLooper()) {

        var continuation: Continuation<Message?>? = null

        override fun handleMessage(msg: Message) {
            onMessage(msg)
        }

        fun onMessage(message: Message?) {
            continuation?.resume(message)
            continuation = null
        }
    }

    enum class ConnectionStatus {
        Connected,
        NotConnected,
        PermissionRequired,
        ;
    }

    private sealed interface SendResult {
        data class Success(val message: Message) : SendResult
        object NotConnected : SendResult
        object PermissionRequired : SendResult
    }
}

suspend inline fun <reified R : Parcelable> ClientToServiceMessenger?.safeSend(request: ServiceRequest<R>): ServiceMessage<R> {
    return this?.send(request)
        ?: ServiceMessage.ConnectionFailure()
}