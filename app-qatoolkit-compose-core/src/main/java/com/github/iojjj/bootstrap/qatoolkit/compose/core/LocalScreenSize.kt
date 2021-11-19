package com.github.iojjj.bootstrap.qatoolkit.compose.core

import android.content.res.Configuration
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.core.content.getSystemService

val LocalScreenSize = compositionLocalOf<IntSize> {
    error("Screen size is not provided.")
}

@Suppress("deprecation")
@Composable
fun rememberScreenSize(): IntSize {
    val context = LocalContext.current
    return remember(context.resources) {
        val windowManager = context.getSystemService<WindowManager>()!!
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(displayMetrics)
        when (context.resources.configuration.orientation) {
            Configuration.ORIENTATION_UNDEFINED,
            Configuration.ORIENTATION_PORTRAIT -> {
                IntSize(
                    displayMetrics.widthPixels,
                    displayMetrics.heightPixels,
                )
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                IntSize(
                    displayMetrics.widthPixels,
                    displayMetrics.heightPixels,
                )
            }
            else -> {
                IntSize.Zero
            }
        }

    }
}