package com.github.iojjj.bootstrap.internal.qatoolkit.bridge

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.ViewCompat
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.github.iojjj.bootstrap.pub.core.application.ActivityLifecycleCallbacksAdapter
import com.github.iojjj.bootstrap.pub.core.exhaustive
import com.github.iojjj.bootstrap.qatoolkit.bridge.R
import java.lang.ref.WeakReference

/**
 * Layout observer that injects custom accessibility delegate to every view of any launched activity.
 */
@SuppressLint("StaticFieldLeak")
internal object QaToolkitLayoutObserver {

    private val activityContentObserver = ActivityContentObserver()
    private var bridgeServiceRunner: BridgeServiceRunner? = null

    val contentRef: WeakReference<View>
        get() = activityContentObserver.contentRef

    /**
     * Starts observation.
     * @param application instance of [Application]
     */
    fun start(application: Application) {
        application.registerActivityLifecycleCallbacks(activityContentObserver)
        ProcessLifecycleOwner.get().lifecycle.addObserver(
            BridgeServiceRunner(application).also { bridgeServiceRunner = it }
        )
    }

    /**
     * Stops observation.
     * @param application instance of [Application]
     */
    fun stop(application: Application) {
        bridgeServiceRunner?.also {
            ProcessLifecycleOwner.get().lifecycle.removeObserver(it)
            it.cleanUp()
        }
        application.unregisterActivityLifecycleCallbacks(activityContentObserver)
        activityContentObserver.cleanUp()
    }

    private fun View.updateAccessibilityDelegate() {
        val old = ViewCompat.getAccessibilityDelegate(this)
        if (old !is QaToolkitAccessibilityDelegate) {
            ViewCompat.setAccessibilityDelegate(this, QaToolkitAccessibilityDelegate(old))
        }
        if (this is ViewGroup) {
            children.forEach {
                it.updateAccessibilityDelegate()
            }
        }
    }

    private class ActivityContentObserver : ActivityLifecycleCallbacksAdapter() {

        var contentRef: WeakReference<View> = WeakReference(null)

        override fun onActivityStarted(activity: Activity) {
            val content = activity.findViewById<ViewGroup>(android.R.id.content)
                .children
                .first().apply {
                    val listener = getTag(R.id.qatoolkit_global_layout_listener) as? ViewTreeObserver.OnGlobalLayoutListener
                    if (listener == null) {
                        viewTreeObserver.addOnGlobalLayoutListener {
                            updateAccessibilityDelegate()
                        }
                        setTag(R.id.qatoolkit_global_layout_listener, true)
                    }
                }
            contentRef = WeakReference(content)
        }

        override fun onActivityStopped(activity: Activity) {
            val content = activity.findViewById<ViewGroup>(android.R.id.content)
                .children
                .first().apply {
                    val listener = getTag(R.id.qatoolkit_global_layout_listener) as? ViewTreeObserver.OnGlobalLayoutListener
                    if (listener != null) {
                        viewTreeObserver.removeOnGlobalLayoutListener(listener)
                        setTag(R.id.qatoolkit_global_layout_listener, null)
                    }
                }
            if (contentRef.get() == content) {
                contentRef.clear()
            }
        }

        fun cleanUp() {
            contentRef.clear()
        }
    }

    private class BridgeServiceRunner(
        private val context: Context
    ) : LifecycleEventObserver {

        override fun onStateChanged(
            source: LifecycleOwner,
            event: Lifecycle.Event
        ) {
            exhaustive..when (event) {
                Lifecycle.Event.ON_START -> {
                    context.startService(Intent(context, QaToolkitBridgeService::class.java))
                }
                Lifecycle.Event.ON_STOP -> {
                    context.stopService(Intent(context, QaToolkitBridgeService::class.java))
                }
                Lifecycle.Event.ON_CREATE,
                Lifecycle.Event.ON_RESUME,
                Lifecycle.Event.ON_PAUSE,
                Lifecycle.Event.ON_DESTROY,
                Lifecycle.Event.ON_ANY -> {
                    /* no-op */
                }
            }
        }

        fun cleanUp() {
            context.stopService(Intent(context, QaToolkitBridgeService::class.java))
        }
    }
}