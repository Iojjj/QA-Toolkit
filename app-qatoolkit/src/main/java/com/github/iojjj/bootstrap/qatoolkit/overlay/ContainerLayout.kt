package com.github.iojjj.bootstrap.qatoolkit.overlay

import android.annotation.SuppressLint
import android.content.Context
import android.view.ActionMode
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.view.SupportMenuInflater
import androidx.appcompat.view.menu.MenuBuilder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.AbstractComposeView

class ContainerLayout constructor(
    context: Context,
) : AbstractComposeView(context) {

    private val content = mutableStateOf<(@Composable () -> Unit)?>(null)

    var onBackKeyPressListener: (() -> Unit)? = null

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return if (event.action == KeyEvent.ACTION_UP && event.keyCode == KeyEvent.KEYCODE_BACK) {
            onBackKeyPressListener
                ?.invoke()
                ?.let { true }
                ?: false
        } else {
            super.dispatchKeyEvent(event)
        }
    }

    @Suppress("RedundantVisibilityModifier")
    protected override var shouldCreateCompositionOnAttachedToWindow: Boolean = false
        private set

    @Composable
    override fun Content() {
        content.value?.invoke()
    }

    /**
     * Set the Jetpack Compose UI content for this view.
     * Initial composition will occur when the view becomes attached to a window or when
     * [createComposition] is called, whichever comes first.
     */
    fun setContent(content: @Composable () -> Unit) {
        shouldCreateCompositionOnAttachedToWindow = true
        this.content.value = content
        if (isAttachedToWindow) {
            createComposition()
        }
    }

    // FIXME hack to avoid crashes when user tries to invoke selection menu in TextField
    override fun startActionModeForChild(
        originalView: View?,
        callback: ActionMode.Callback?,
        type: Int
    ): ActionMode {
        return object : ActionMode() {

            private var title: CharSequence? = null
            private var subtitle: CharSequence? = null
            private var customView: View? = null

            override fun setTitle(title: CharSequence?) {
                this.title = title
            }

            override fun setTitle(resId: Int) {
                /* no-op */
            }

            override fun setSubtitle(subtitle: CharSequence?) {
                this.subtitle = subtitle
            }

            override fun setSubtitle(resId: Int) {
                /* no-op */
            }

            override fun setCustomView(view: View?) {
                this.customView = view
            }

            override fun invalidate() {
                /* no-op */
            }

            override fun finish() {
                customView = null
            }

            @SuppressLint("RestrictedApi")
            override fun getMenu(): Menu {
                return MenuBuilder(context)
            }

            override fun getTitle(): CharSequence? {
                return title
            }

            override fun getSubtitle(): CharSequence? {
                return subtitle
            }

            override fun getCustomView(): View? {
                return customView
            }

            @SuppressLint("RestrictedApi")
            override fun getMenuInflater(): MenuInflater {
                return SupportMenuInflater(context)
            }
        }
    }
}