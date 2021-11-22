package com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.service

import android.os.Message
import android.os.Parcelable
import com.github.iojjj.bootstrap.pub.core.os.makeBundle
import com.github.iojjj.bootstrap.pub.core.os.withCurrentThreadContextClassLoader
import kotlinx.parcelize.Parcelize

object ServiceResponse {

    private const val MSG_RESPONSE = 20_000
    private const val KEY_RESPONSE = "key_response"

    fun toMessage(request: Parcelable): Message {
        return Message.obtain(null, MSG_RESPONSE).apply {
            data = makeBundle {
                KEY_RESPONSE to request
            }
        }
    }

    fun fromMessage(message: Message): Result<Parcelable?> {
        return when (message.what) {
            MSG_RESPONSE -> {
                Result.success(message.data.withCurrentThreadContextClassLoader {
                    getParcelable(KEY_RESPONSE)
                })
            }
            else -> {
                Result.failure(RuntimeException())
            }
        }
    }

    @Parcelize
    object Unit : Parcelable
}