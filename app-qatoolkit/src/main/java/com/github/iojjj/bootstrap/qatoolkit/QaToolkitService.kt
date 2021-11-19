package com.github.iojjj.bootstrap.qatoolkit

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.view.accessibility.AccessibilityWindowInfo
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalView
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.compose.currentBackStackEntryAsState
import com.github.iojjj.bootstrap.pub.core.exhaustive
import com.github.iojjj.bootstrap.pub.core.extensions.hasFlag
import com.github.iojjj.bootstrap.pub.core.view.accessibility.children
import com.github.iojjj.bootstrap.pub.core.view.accessibility.isValid
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_CHECKABLE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_CHECKED
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_CLICKABLE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_ENABLED
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_FOCUSABLE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_FOCUSED
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_HEIGHT
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_ID
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_LONG_CLICKABLE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_TYPE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.ATTRIBUTE_WIDTH
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CATEGORY_NAME_MAIN
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CATEGORY_VIEW_LAYOUT
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CATEGORY_VIEW_STATE
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.boolean
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.CommonAttribute.Companion.string
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.attribute.DimensionAttribute.Companion.dimension
import com.github.iojjj.bootstrap.pub.qatoolkit.inspector.layout.CategorizedAttributes
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalDpFormatter
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalHasBridge
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalPercentFormatter
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalPxFormatter
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalScreenSize
import com.github.iojjj.bootstrap.qatoolkit.compose.core.LocalSizeFormatter
import com.github.iojjj.bootstrap.qatoolkit.compose.core.rememberDpFormatter
import com.github.iojjj.bootstrap.qatoolkit.compose.core.rememberPercentFormatter
import com.github.iojjj.bootstrap.qatoolkit.compose.core.rememberPxFormatter
import com.github.iojjj.bootstrap.qatoolkit.compose.core.rememberScreenSize
import com.github.iojjj.bootstrap.qatoolkit.compose.core.rememberSizeFormatter
import com.github.iojjj.bootstrap.qatoolkit.inspector.ViewNode
import com.github.iojjj.bootstrap.qatoolkit.overlay.ContentChange
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitContentOverlayManager
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitContentOverlayManager.Companion.canDrawOverlays
import com.github.iojjj.bootstrap.qatoolkit.overlay.ToolkitMode
import com.github.iojjj.bootstrap.qatoolkit.overlay.ViewModelFactory
import com.github.iojjj.bootstrap.qatoolkit.pool.Pool
import com.github.iojjj.bootstrap.qatoolkit.pool.SimplePool
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.EXTRA_BRIDGE_CONFIGURED
import com.github.iojjj.bootstrap.qatoolkit.pub.bridge.core.EXTRA_BRIDGE_UUID
import com.github.iojjj.bootstrap.qatoolkit.settings.SettingsActivity
import com.github.iojjj.bootstrap.qatoolkit.theme.QaToolkitTheme
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.ToolkitBarOverlay
import com.google.accompanist.insets.ProvideWindowInsets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow

class QaToolkitService : AccessibilityService() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var lastAccessibilityNode: AccessibilityNodeInfo? = null
    private var lastViewNode: ViewNode? = null

    private var toolkitBarOverlayManager: ToolkitContentOverlayManager? = null
    private var toolkitContentOverlayManager: ToolkitContentOverlayManager? = null
    private val hasBridgeState = MutableStateFlow(false)
    private val isNightModeState = MutableStateFlow(false)

    private val rectPool: Pool<Rect> = SimplePool(::Rect)
    private val navController by lazy {
        NavHostController(this).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            navigatorProvider.addNavigator(DialogNavigator())
        }
    }

    override fun onCreate() {
        super.onCreate()
        isNightModeState.value = resources.configuration.uiMode.hasFlag(UI_MODE_NIGHT_YES, UI_MODE_NIGHT_MASK)
        if (canDrawOverlays(this)) {
            onReadyDrawOverlays()
        } else {
            openSettingsScreen()
        }
    }

    override fun onInterrupt() {
        /* no-op */
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        exhaustive..when (event.eventType) {
            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED,
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                if (hasActiveApplicationWindow()) {
                    updateMessengerConnection(rootInActiveWindow)
                }
                Unit
            }
            else -> {
                /* no-op */
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        isNightModeState.value = newConfig.uiMode.hasFlag(UI_MODE_NIGHT_YES, UI_MODE_NIGHT_MASK)
    }

    override fun onDestroy() {
        cleanUp()
        serviceScope.cancel()
        super.onDestroy()
    }

    private fun cleanUp() {
        toolkitBarOverlayManager?.hide()
        toolkitContentOverlayManager?.hide()
        toolkitBarOverlayManager?.cleanUp()
        toolkitContentOverlayManager?.cleanUp()
        toolkitBarOverlayManager = null
        toolkitContentOverlayManager = null
        lastViewNode = null
        lastAccessibilityNode?.recycle()
        lastAccessibilityNode = null
    }

    private fun onReadyDrawOverlays() {
        val contentOverlay = ToolkitContentOverlayManager(this).apply {
            onDrawOverlaysPermissionRequired = ::onDrawOverlaysPermissionRequired
        }
        val barOverlay = ToolkitContentOverlayManager(this).apply {
            onDrawOverlaysPermissionRequired = ::onDrawOverlaysPermissionRequired
        }

        // Content overlay attached first to be below of toolkit bar
        contentOverlay.show()
        barOverlay.show()

        toolkitBarOverlayManager = barOverlay
        toolkitContentOverlayManager = contentOverlay
        setupOverlays(barOverlay, contentOverlay)
    }

    private fun onDrawOverlaysPermissionRequired() {
        cleanUp()
        openSettingsScreen()
    }

    private fun setupOverlays(
        toolkitBarOverlayManager: ToolkitContentOverlayManager,
        toolkitContentOverlayManager: ToolkitContentOverlayManager
    ) {
        toolkitContentOverlayManager.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigateUp()
            }
        })
        val navigateTo: (ToolkitMode) -> Unit = {
            navController.navigate(it)
        }
        val sharedPreferences = getSharedPreferences("qatoolkit_overlay_state", Context.MODE_PRIVATE)
        val viewModelFactory = ViewModelFactory(applicationContext, navigateTo, sharedPreferences, isNightModeState)
        toolkitBarOverlayManager.setContent(
            initialVisibility = ContentChange.ContentVisible(
                width = WRAP_CONTENT,
                height = WRAP_CONTENT,
                isLayoutInScreen = true,
                isFocusable = false,
                isTouchable = true,
                isNotTouchModal = true,
                isImeInputRequired = false,
            )
        ) { callback ->
            QaToolkitTheme {
                val hasBridge = hasBridgeState.collectAsState()
                CompositionLocalProvider(
                    LocalHasBridge provides hasBridge.value,
                ) {
                    ToolkitBarOverlay(navController, viewModelFactory, callback)
                }
            }
        }
        toolkitContentOverlayManager.setContent { callback ->
            QaToolkitTheme {
                NotifyInsetsChangedEffect()
                ProvideWindowInsets {
                    CompositionLocalProvider(
                        LocalScreenSize provides rememberScreenSize(),
                        LocalSizeFormatter provides rememberSizeFormatter(),
                        LocalPercentFormatter provides rememberPercentFormatter(),
                        LocalDpFormatter provides rememberDpFormatter(),
                        LocalPxFormatter provides rememberPxFormatter(),
                    ) {
                        ToolkitContentOverlay(
                            findRootViewNode = ::findRootViewNode,
                            navController = navController,
                            onBackPressedDispatcher = toolkitContentOverlayManager.onBackPressedDispatcher,
                            viewModelFactory = viewModelFactory
                        )
                    }
                }
            }

            val backStackEntry by navController.currentBackStackEntryAsState()
            val toolkitMode = backStackEntry?.route<ToolkitMode>()
            LaunchedEffect(toolkitMode) {
                if (toolkitMode == ToolkitMode.Initial || toolkitMode == null) {
                    callback(ContentChange.ContentNotVisible)
                } else {
                    callback(
                        ContentChange.ContentVisible(
                            width = MATCH_PARENT,
                            height = MATCH_PARENT,
                            isLayoutInScreen = true,
                            isFocusable = true,
                            isTouchable = true,
                            isNotTouchModal = true,
                            isImeInputRequired = true,
                        )
                    )
                }
            }
        }
    }

    @Composable
    private fun NotifyInsetsChangedEffect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return
        }
        val view = LocalView.current
        DisposableEffect(view) {
            val listener = View.OnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
                v.requestApplyInsets()
            }
            view.addOnLayoutChangeListener(listener)
            onDispose {
                view.removeOnLayoutChangeListener(listener)
            }
        }
    }

    private fun openSettingsScreen() {
        startActivity(Intent(this, SettingsActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
    }

    private fun hasActiveApplicationWindow(): Boolean {
        return windows.firstOrNull { it.type == AccessibilityWindowInfo.TYPE_APPLICATION && it.isActive } != null
    }

    private fun AccessibilityNodeInfo.findBridgeBundle(): Bundle? {
        return when {
            extras.containsKey(EXTRA_BRIDGE_CONFIGURED) -> {
                extras
            }
            else -> {
                children.mapNotNull { it.findBridgeBundle() }
                    .firstOrNull()
            }
        }
    }

    private fun updateMessengerConnection(accessibilityNodeInfo: AccessibilityNodeInfo?) {
        hasBridgeState.value = accessibilityNodeInfo?.findBridgeBundle()
            ?.getBoolean(EXTRA_BRIDGE_CONFIGURED, false)
            ?: false
    }

    private fun refreshLastAccessibilityNode(): AccessibilityNodeInfo? {
        val localLastAccessibilityNode = lastAccessibilityNode
        return if (localLastAccessibilityNode != null && localLastAccessibilityNode.isValid && localLastAccessibilityNode.refresh()) {
            localLastAccessibilityNode
        } else {
            localLastAccessibilityNode?.recycle()
            lastAccessibilityNode = rootInActiveWindow?.takeIf { hasActiveApplicationWindow() }
            lastAccessibilityNode
        }
        // return if (
        //     localLastAccessibilityNode == null
        //     // || localLastAccessibilityNode.className.isNullOrEmpty()
        //     || currentRoot != null && currentRoot != localLastAccessibilityNode
        // ) {
        //     if (localLastAccessibilityNode?.isValid == true) {
        //         localLastAccessibilityNode.recycle()
        //     }
        //     lastAccessibilityNode = currentRoot
        //     // val activeRoot = currentRoot?.takeUnless {
        //     //     it.packageName == packageName && it.className == View::class.qualifiedName
        //     // }
        //     // if (activeRoot != null && !activeRoot.className.isNullOrEmpty()) {
        //     //     val possibleContent = activeRoot.findAccessibilityNodeInfosByViewId("android:id/content")
        //     //     lastAccessibilityNode = possibleContent
        //     //         .firstOrNull()
        //     //         ?.children
        //     //         ?.firstOrNull()
        //     //         // Fallback to current root (maybe it's a popup/dialog).
        //     //         ?: activeRoot
        //     //     possibleContent.forEach {
        //     //         it.recycle()
        //     //     }
        //     // }
        //     lastAccessibilityNode
        // } else {
        //     whenever(localLastAccessibilityNode.isValid) {
        //         if (!localLastAccessibilityNode.refresh()) {
        //             lastAccessibilityNode?.recycle()
        //             lastAccessibilityNode = null
        //         }
        //         localLastAccessibilityNode
        //     }
        // }
    }

    private fun findRootViewNode(): ViewNode? {
        return refreshLastAccessibilityNode()
            ?.also { updateMessengerConnection(it) }
            ?.captureRootViewNode()
            ?: lastViewNode
    }

    private fun AccessibilityNodeInfo.captureRootViewNode(): ViewNode? {
        val isBridgeConfigured = findBridgeBundle()
            ?.getBoolean(EXTRA_BRIDGE_CONFIGURED, false)
            ?: false
        return captureHierarchy(isBridgeConfigured)?.also {
            lastViewNode = it
        }
    }

    private fun AccessibilityNodeInfo.captureHierarchy(isBridgeConfigured: Boolean): ViewNode? {
        // Ignore views not visible to user, they can't be selected.
        return if (isVisibleToUser) {
            val id = viewIdResourceName
                ?.split("/")
                ?.last()
            val bounds = rectPool.acquire().apply {
                getBoundsInScreen(this)
            }
            val uuid = extras.getString(EXTRA_BRIDGE_UUID)
            val orderedChildren = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                children.sortedByDescending { it.drawingOrder }
            } else {
                children.toList().reversed().asSequence()
            }
            val defaultAttributes = loadDefaultAttributes(id, bounds)
            return ViewNode(
                id = id,
                bounds = bounds.toComposeRect().also {
                    rectPool.release(bounds)
                },
                children = orderedChildren.mapNotNull { it.captureHierarchy(isBridgeConfigured) }
                    .toList(),
                bridgeInfo = when {
                    isBridgeConfigured && !uuid.isNullOrEmpty() -> {
                        BridgeInfo.Available(
                            uuid = uuid,
                            servicePackageName = packageName.toString(),
                            categories = defaultAttributes,
                        )
                    }
                    isBridgeConfigured -> {
                        BridgeInfo.ViewNotConfigured(defaultAttributes)
                    }
                    else -> {
                        BridgeInfo.NotAvailable(defaultAttributes)
                    }
                }
            )
        } else {
            null
        }
    }

    private fun AccessibilityNodeInfo.loadDefaultAttributes(
        id: String?,
        bounds: Rect,
    ): List<CategorizedAttributes> {
        return listOf(
            CategorizedAttributes(
                name = CATEGORY_NAME_MAIN,
                attributes = listOfNotNull(
                    id?.let { string(ATTRIBUTE_ID, it) },
                    string(ATTRIBUTE_TYPE, className.toString()),
                )
            ),
            CategorizedAttributes(
                name = CATEGORY_VIEW_LAYOUT,
                attributes = listOf(
                    dimension(ATTRIBUTE_WIDTH, bounds.width().toFloat()),
                    dimension(ATTRIBUTE_HEIGHT, bounds.height().toFloat()),
                )
            ),
            CategorizedAttributes(
                name = CATEGORY_VIEW_STATE,
                attributes = listOf(
                    boolean(ATTRIBUTE_ENABLED, isEnabled),
                    boolean(ATTRIBUTE_CHECKABLE, isCheckable),
                    boolean(ATTRIBUTE_CHECKED, isChecked),
                    boolean(ATTRIBUTE_CLICKABLE, isClickable),
                    boolean(ATTRIBUTE_LONG_CLICKABLE, isLongClickable),
                    boolean(ATTRIBUTE_FOCUSABLE, isFocusable),
                    boolean(ATTRIBUTE_FOCUSED, isFocused),
                )
            )
        )
    }

    companion object {

        const val ACTION_CAN_DRAW_OVERLAYS = "ACTION_CAN_DRAW_OVERLAYS"
    }
}