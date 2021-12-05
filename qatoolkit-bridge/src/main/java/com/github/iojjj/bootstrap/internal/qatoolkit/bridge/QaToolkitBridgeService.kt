package com.github.iojjj.bootstrap.internal.qatoolkit.bridge

import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.children
import com.github.iojjj.bootstrap.pub.core.exhaustive
import com.github.iojjj.bootstrap.pub.logger.Logger
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.layout.LayoutInspector
import com.github.iojjj.bootstrap.qatoolkit.bridge.R
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.BridgeMessage
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.BridgeMessage.LoadAttributesRequest
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.BridgeMessage.LoadAttributesRequest.Response.ViewFound
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.BridgeMessage.LoadAttributesRequest.Response.ViewNotFound
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.service.ClientMessage
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.service.CommunicationService
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.service.of
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.service.replyWith

/**
 * Bridge service that allows to retrieve extra information about views.
 */
internal class QaToolkitBridgeService : CommunicationService<BridgeMessage>(BridgeMessage::class) {

    override fun onCreate() {
        super.onCreate()
        NotificationManagerCompat.from(this).run {
            if (getNotificationChannelCompat(BRIDGE_NOTIFICATION_CHANNEL) == null) {
                createNotificationChannel(
                    NotificationChannelCompat.Builder(BRIDGE_NOTIFICATION_CHANNEL, NotificationManagerCompat.IMPORTANCE_MIN)
                        .setName("QA Toolkit Bridge Service")
                        .setSound(null, null)
                        .setShowBadge(false)
                        .setLightsEnabled(false)
                        .setVibrationEnabled(false)
                        .build()
                )
            }
            notify(
                Int.MIN_VALUE,
                NotificationCompat.Builder(this@QaToolkitBridgeService, BRIDGE_NOTIFICATION_CHANNEL)
                    .setSmallIcon(R.drawable.qatoolkit_bridge_ic_bug_24dp)
                    .setShowWhen(false)
                    .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .setLocalOnly(true)
                    .setOngoing(true)
                    .setContentText("QA Toolkit Bridge Service is running")
                    .build()
            )
        }
    }

    override fun onDestroy() {
        NotificationManagerCompat.from(this).cancel(Int.MIN_VALUE)
        super.onDestroy()
    }

    override fun onMessageReceived(message: ClientMessage<BridgeMessage>) {
        exhaustive..when (message) {
            is ClientMessage.Received -> {
                when (val request = message.request) {
                    is LoadAttributesRequest -> {
                        val view = QaToolkitLayoutObserver.contentRef.get()?.findViewByUuid(request.uuid)
                        message.of(request).replyWith(
                            if (view == null) {
                                ViewNotFound
                            } else {
                                ViewFound(
                                    uuid = request.uuid,
                                    categories = LayoutInspector.inspect(view),
                                )
                            }
                        )
                    }
                }
            }
            is ClientMessage.UnsupportedType -> {
                Logger.Default.error { "Received service request of unsupported type: ${message.request}." }
            }
            is ClientMessage.Unknown -> {
                Logger.Default.error { "Received unknown message: ${message.message}." }
            }
        }
    }

    companion object {

        private const val BRIDGE_NOTIFICATION_CHANNEL = "qatoolkit_bridge_channel"

        private fun View.findViewByUuid(uuid: String): View? {
            return when {
                getTag(R.id.qatoolkit_uuid) as? String == uuid -> {
                    this
                }
                this is ViewGroup -> {
                    children.mapNotNull { it.findViewByUuid(uuid) }
                        .firstOrNull()
                }
                else -> {
                    null
                }
            }
        }
    }
}

