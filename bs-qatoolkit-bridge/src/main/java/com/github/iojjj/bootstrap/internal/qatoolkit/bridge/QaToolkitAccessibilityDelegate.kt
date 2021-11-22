package com.github.iojjj.bootstrap.internal.qatoolkit.bridge

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.core.view.accessibility.AccessibilityNodeProviderCompat
import com.github.iojjj.bootstrap.qatoolkit.bridge.R
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.EXTRA_BRIDGE_CONFIGURED
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.EXTRA_BRIDGE_UUID
import java.util.UUID

/**
 * Implementation of accessibility delegate that marks view with unique id.
 * @property delegate Optional delegate.
 * @property uuid Unique view id.
 */
internal class QaToolkitAccessibilityDelegate(
    private val delegate: AccessibilityDelegateCompat? = null
) : AccessibilityDelegateCompat() {

    private val uuid = UUID.randomUUID().toString()

    override fun onInitializeAccessibilityEvent(
        host: View?,
        event: AccessibilityEvent?
    ) {
        delegate?.onInitializeAccessibilityEvent(host, event)
            ?: super.onInitializeAccessibilityEvent(host, event)
    }

    override fun onInitializeAccessibilityNodeInfo(
        host: View,
        info: AccessibilityNodeInfoCompat
    ) {
        delegate?.onInitializeAccessibilityNodeInfo(host, info)
            ?: super.onInitializeAccessibilityNodeInfo(host, info)
        host.setTag(R.id.qatoolkit_uuid, uuid)
        info.extras.putBoolean(EXTRA_BRIDGE_CONFIGURED, true)
        info.extras.putString(EXTRA_BRIDGE_UUID, uuid)
    }

    override fun sendAccessibilityEvent(
        host: View?,
        eventType: Int
    ) {
        delegate?.sendAccessibilityEvent(host, eventType)
            ?: super.sendAccessibilityEvent(host, eventType)
    }

    override fun sendAccessibilityEventUnchecked(
        host: View?,
        event: AccessibilityEvent?
    ) {
        delegate?.sendAccessibilityEventUnchecked(host, event)
            ?: super.sendAccessibilityEventUnchecked(host, event)
    }

    override fun dispatchPopulateAccessibilityEvent(
        host: View?,
        event: AccessibilityEvent?
    ): Boolean {
        return delegate?.dispatchPopulateAccessibilityEvent(host, event)
            ?: super.dispatchPopulateAccessibilityEvent(host, event)
    }

    override fun onPopulateAccessibilityEvent(
        host: View?,
        event: AccessibilityEvent?
    ) {
        delegate?.onPopulateAccessibilityEvent(host, event)
            ?: super.onPopulateAccessibilityEvent(host, event)
    }

    override fun onRequestSendAccessibilityEvent(
        host: ViewGroup?,
        child: View?,
        event: AccessibilityEvent?
    ): Boolean {
        return delegate?.onRequestSendAccessibilityEvent(host, child, event)
            ?: super.onRequestSendAccessibilityEvent(host, child, event)
    }

    override fun getAccessibilityNodeProvider(host: View?): AccessibilityNodeProviderCompat? {
        return delegate?.getAccessibilityNodeProvider(host)
            ?: super.getAccessibilityNodeProvider(host)
    }

    override fun performAccessibilityAction(
        host: View?,
        action: Int,
        args: Bundle?
    ): Boolean {
        return delegate?.performAccessibilityAction(host, action, args)
            ?: super.performAccessibilityAction(host, action, args)
    }
}