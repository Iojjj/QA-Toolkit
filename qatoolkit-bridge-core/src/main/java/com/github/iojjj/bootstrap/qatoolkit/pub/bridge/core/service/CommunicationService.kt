package com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlin.reflect.KClass

abstract class CommunicationService<T: Any>(messageType: KClass<T>) : Service() {

    private val messenger = ServiceToClientMessenger(messageType, ::onMessageReceived)

    final override fun onBind(intent: Intent?): IBinder? {
        return messenger.binder
    }

    abstract fun onMessageReceived(message: ClientMessage<T>)

}