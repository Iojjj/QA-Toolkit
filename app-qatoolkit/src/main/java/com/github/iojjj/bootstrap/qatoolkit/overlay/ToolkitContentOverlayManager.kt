@file:Suppress("DEPRECATION")

package com.github.iojjj.bootstrap.qatoolkit.overlay

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
import android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
import android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
import android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
import android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
import android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
import android.view.WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import android.view.WindowManager.LayoutParams.TYPE_PHONE
import android.view.WindowManager.LayoutParams.WRAP_CONTENT
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector2D
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.ViewTreeLifecycleOwner
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import androidx.savedstate.ViewTreeSavedStateRegistryOwner
import com.github.iojjj.bootstrap.pub.core.exhaustive
import com.github.iojjj.bootstrap.pub.core.stdlib.hasFlag
import com.github.iojjj.bootstrap.pub.coroutines.view.delayUntilNextLayout
import com.github.iojjj.bootstrap.qatoolkit.R
import com.github.iojjj.bootstrap.qatoolkit.compose.core.unit.maxDimension
import com.github.iojjj.bootstrap.qatoolkit.compose.core.unit.minDimension
import com.github.iojjj.bootstrap.qatoolkit.core.Orientation
import com.google.accompanist.insets.Insets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ToolkitContentOverlayManager(context: Context) :
    OnBackPressedDispatcherOwner {

    private var isShown = false
    private var isCleanedUp = false
    private val windowManager = context.getSystemService<WindowManager>()!!
    private val composeComponentsOwner = ComposeComponentsOwner()
    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val onBackPressedDispatcher = OnBackPressedDispatcher()

    private val containerLayoutParams = makeLayoutParams()

    private val containerLayout = ContainerLayout(ContextThemeWrapper(context, R.style.Theme_Toolkit)).apply {
        composeComponentsOwner.attach(this)
        composeComponentsOwner.onCreate()
        onBackKeyPressListener = onBackPressedDispatcher::onBackPressed
    }

    var onDrawOverlaysPermissionRequired: (() -> Unit)? = null

    override fun getOnBackPressedDispatcher(): OnBackPressedDispatcher {
        return onBackPressedDispatcher
    }

    override fun getLifecycle(): Lifecycle {
        return composeComponentsOwner.lifecycle
    }

    fun show() {
        if (isCleanedUp) {
            throw IllegalStateException()
        }
        if (isShown) {
            return
        }
        if (canDrawOverlays(containerLayout.context)) {
            windowManager.addView(containerLayout, containerLayoutParams)
            composeComponentsOwner.onResume()
            isShown = true
        } else {
            onDrawOverlaysPermissionRequired?.invoke()
        }
    }

    fun hide() {
        if (isCleanedUp) {
            throw IllegalStateException()
        }
        if (!isShown) {
            return
        }
        composeComponentsOwner.onPause()
        windowManager.removeViewImmediate(containerLayout)
        isShown = false
    }

    fun cleanUp() {
        if (isCleanedUp) {
            return
        }
        if (isShown) {
            hide()
        }
        composeComponentsOwner.onDestroy()
        composeComponentsOwner.detach(containerLayout)
        coroutineScope.cancel()
        isCleanedUp = true
    }

    fun setContent(
        initialVisibility: ContentChange.VisibilityChange = ContentChange.ContentNotVisible,
        content: @Composable (
            callback: @DisallowComposableCalls (contentChange: ContentChange) -> Unit,
        ) -> Unit,
    ) {
        handleContentChange(initialVisibility)
        val onHandleContentChange = ::handleContentChange
        containerLayout.setContent {
            content(onHandleContentChange)
        }
    }

    private fun handleContentChange(contentChange: ContentChange) {
        exhaustive..when (contentChange) {
            ContentChange.ContentNotVisible -> {
                hideContent()
            }
            is ContentChange.ContentVisible -> {
                showContent(contentChange)
            }
            is ContentChange.ChangeSize -> {
                updateLayoutParams(newWidth = contentChange.width, newHeight = contentChange.height)
            }
            is ContentChange.UpdatePosition -> {
                updateLayoutParams(newX = contentChange.x, newY = contentChange.y)
            }
        }
    }

    private fun resetFlags(): Int {
        return containerLayoutParams.flags and
            FLAG_LAYOUT_IN_SCREEN.inv() and
            FLAG_LAYOUT_INSET_DECOR.inv() and
            FLAG_NOT_FOCUSABLE.inv() and
            FLAG_NOT_TOUCHABLE.inv() and
            FLAG_NOT_TOUCH_MODAL.inv() and
            FLAG_ALT_FOCUSABLE_IM.inv()
    }

    private fun resetSystemUiVisibility(): Int {
        return containerLayoutParams.systemUiVisibility and
            SYSTEM_UI_FLAG_LAYOUT_STABLE.inv() and
            SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.inv() and
            SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION.inv()
    }

    private fun showContent(contentChange: ContentChange.ContentVisible) {
        var newFlags = resetFlags()
        var newSystemUiVisibility = resetSystemUiVisibility()

        if (contentChange.isLayoutInScreen) {
            newFlags = newFlags or FLAG_LAYOUT_IN_SCREEN
            newSystemUiVisibility = newSystemUiVisibility or
                SYSTEM_UI_FLAG_LAYOUT_STABLE or
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        }
        if (contentChange.isFocusable) {
            if (!contentChange.isImeInputRequired) {
                newFlags = newFlags or FLAG_ALT_FOCUSABLE_IM
            }
        } else {
            newFlags = newFlags or FLAG_NOT_FOCUSABLE
            if (contentChange.isImeInputRequired) {
                newFlags = newFlags or FLAG_ALT_FOCUSABLE_IM
            }
        }
        if (!contentChange.isTouchable) {
            newFlags = newFlags or FLAG_NOT_TOUCHABLE
        }
        if (contentChange.isNotTouchModal) {
            newFlags = newFlags or FLAG_NOT_TOUCH_MODAL
        }
        val newWidth = contentChange.width
        val newHeight = contentChange.height
        updateLayoutParams(newWidth, newHeight, newFlags, newSystemUiVisibility)
    }

    private fun hideContent() {
        val newFlags = resetFlags() or
            FLAG_NOT_TOUCHABLE or
            FLAG_NOT_FOCUSABLE
        val newSystemUiVisibility = resetSystemUiVisibility()
        coroutineScope.launch {
            // When showing a new content, by some reason window first displays previous content.
            // To avoid this, first change size to 1px, then to 0px.
            updateLayoutParams(newWidth = 1, newHeight = 1)
            containerLayout.delayUntilNextLayout()
            updateLayoutParams(newWidth = 0, newHeight = 0, newFlags, newSystemUiVisibility)
        }
    }

    private fun updateLayoutParams(
        newWidth: Int = containerLayoutParams.width,
        newHeight: Int = containerLayoutParams.height,
        newFlags: Int = containerLayoutParams.flags,
        newSystemUiVisibility: Int = containerLayoutParams.systemUiVisibility,
        newX: Int = containerLayoutParams.x,
        newY: Int = containerLayoutParams.y,
    ) {
        if (canDrawOverlays(containerLayout.context)) {
            if (containerLayoutParams.width != newWidth ||
                containerLayoutParams.height != newHeight ||
                containerLayoutParams.flags != newFlags ||
                containerLayoutParams.systemUiVisibility != newSystemUiVisibility ||
                containerLayoutParams.x != newX ||
                containerLayoutParams.y != newY
            ) {
                windowManager.updateViewLayout(containerLayout, containerLayoutParams.apply {
                    x = newX
                    y = newY
                    width = newWidth
                    height = newHeight
                    updateFlags(newFlags)
                    systemUiVisibility = newSystemUiVisibility
                })
            }
        } else {
            hide()
            onDrawOverlaysPermissionRequired?.invoke()
        }
    }

    private class ComposeComponentsOwner :
        LifecycleOwner,
        SavedStateRegistryOwner,
        ViewModelStoreOwner {

        private val lifecycleRegistry = LifecycleRegistry(this)
        private val savedStateRegistryController = SavedStateRegistryController.create(this)
        private val viewModelStore = ViewModelStore()

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        override fun getSavedStateRegistry(): SavedStateRegistry {
            return savedStateRegistryController.savedStateRegistry
        }

        override fun getViewModelStore(): ViewModelStore {
            return viewModelStore
        }

        fun attach(view: View) {
            ViewTreeLifecycleOwner.set(view, this)
            ViewTreeSavedStateRegistryOwner.set(view, this)
            ViewTreeViewModelStoreOwner.set(view, this)
        }

        fun detach(view: View) {
            ViewTreeLifecycleOwner.set(view, null)
            ViewTreeSavedStateRegistryOwner.set(view, null)
            ViewTreeViewModelStoreOwner.set(view, null)
        }

        fun onCreate() {
            savedStateRegistryController.performRestore(null)
            lifecycleRegistry.currentState = Lifecycle.State.CREATED
        }

        fun onResume() {
            lifecycleRegistry.currentState = Lifecycle.State.RESUMED
        }

        fun onPause() {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }

        fun onDestroy() {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }
    }

    companion object {

        @Suppress("DEPRECATION")
        private fun makeLayoutParams(): WindowManager.LayoutParams {
            return WindowManager.LayoutParams(
                WRAP_CONTENT,
                WRAP_CONTENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    TYPE_APPLICATION_OVERLAY
                } else {
                    TYPE_PHONE
                },
                0,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.START
            }
        }

        private fun WindowManager.LayoutParams.updateFlags(newFlags: Int) {
            if (flags != newFlags) {
                flags = newFlags
                if (newFlags.hasFlag(FLAG_LAYOUT_IN_SCREEN)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        layoutInDisplayCutoutMode = LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
                    }
                }
            }
        }

        fun Insets.isEmpty(): Boolean {
            return left == 0 && top == 0 && right == 0 && bottom == 0
        }

        fun Insets.isOnlyTopChanged(another: Insets): Boolean {
            return left == another.left && right == another.right && bottom == another.bottom && top != another.top
        }

        fun CoroutineScope.adjustPositionInBounds(
            newOrientation: Orientation,
            screenBounds: IntRect,
            toolkitBarSize: IntSize,
            offset: Animatable<Offset, AnimationVector2D>,
        ): Job? {
            val minDimension = toolkitBarSize.minDimension
            val maxDimension = toolkitBarSize.maxDimension
            return when (newOrientation) {
                Orientation.HORIZONTAL -> {
                    val centerY = offset.value.y + minDimension / 2
                    val top = screenBounds.top.toFloat()
                    val bottom = (screenBounds.bottom - minDimension).coerceAtLeast(screenBounds.top).toFloat()
                    val right = (screenBounds.right - maxDimension).coerceAtLeast(screenBounds.left).toFloat()
                    val newY = if (centerY - top <= bottom - centerY) {
                        top
                    } else {
                        bottom
                    }
                    val newOffset = if (offset.value.x > right) {
                        Offset(right, newY)
                    } else {
                        offset.value.copy(y = newY)
                    }
                    if (newOffset != offset.value) {
                        launch {
                            offset.animateTo(newOffset)
                        }
                    } else {
                        null
                    }
                }
                Orientation.VERTICAL -> {
                    val centerX = offset.value.x + minDimension / 2
                    val left = screenBounds.left.toFloat()
                    val right = (screenBounds.right - minDimension).coerceAtLeast(screenBounds.left).toFloat()
                    val bottom = (screenBounds.bottom - maxDimension).coerceAtLeast(screenBounds.top).toFloat()
                    val newX = if (centerX - left <= right - centerX) {
                        left
                    } else {
                        right
                    }
                    val newOffset = if (offset.value.y > bottom) {
                        Offset(newX, bottom)
                    } else {
                        offset.value.copy(x = newX)
                    }
                    if (newOffset != offset.value) {
                        launch {
                            offset.animateTo(newOffset)
                        }
                    } else {
                        null
                    }
                }
            }
        }

        suspend fun adjustPositionForRotation(
            newOrientation: Orientation,
            screenBounds: IntRect,
            toolkitBarSize: IntSize,
            animatableOffset: Animatable<Offset, AnimationVector2D>,
        ) {
            val width = toolkitBarSize.height
            val height = toolkitBarSize.width
            val offset = animatableOffset.value
            exhaustive..when (newOrientation) {
                Orientation.HORIZONTAL -> {
                    val right = (screenBounds.right - width).coerceAtLeast(screenBounds.left).toFloat()
                    if (offset.x > right) {
                        animatableOffset.animateTo(offset.copy(x = right))
                    }
                    Unit
                }
                Orientation.VERTICAL -> {
                    val bottom = (screenBounds.bottom - height).coerceAtLeast(screenBounds.top).toFloat()
                    if (offset.y > bottom) {
                        animatableOffset.animateTo(offset.copy(y = bottom))
                    }
                    Unit
                }
            }
        }

        fun canDrawOverlays(context: Context): Boolean {
            return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context)
        }
    }
}