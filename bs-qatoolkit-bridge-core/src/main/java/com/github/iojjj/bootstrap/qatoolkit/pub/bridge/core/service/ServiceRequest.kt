package com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.service

import android.os.Message
import android.os.Parcelable
import com.github.iojjj.bootstrap.pub.core.os.makeBundle
import com.github.iojjj.bootstrap.pub.core.os.withCurrentThreadContextClassLoader

interface ServiceRequest<out R : Parcelable> : Parcelable {

    companion object {

        private const val MSG_REQUEST = 10_000
        private const val KEY_REQUEST = "key_request"

        fun toMessage(request: ServiceRequest<Parcelable>): Message {
            return Message.obtain(null, MSG_REQUEST).apply {
                data = makeBundle {
                    KEY_REQUEST to request
                }
            }
        }

        fun fromMessage(message: Message): Result<Parcelable?> {
            return when (message.what) {
                MSG_REQUEST -> {
                    Result.success(message.data.withCurrentThreadContextClassLoader {
                        getParcelable(KEY_REQUEST)
                    })
                }
                else -> {
                    Result.failure(RuntimeException())
                }
            }
        }
    }
}