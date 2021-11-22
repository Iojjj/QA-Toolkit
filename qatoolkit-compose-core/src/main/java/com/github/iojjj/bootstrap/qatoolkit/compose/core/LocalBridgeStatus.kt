package com.github.iojjj.bootstrap.qatoolkit.compose.core

import androidx.compose.runtime.compositionLocalOf

val LocalHasBridge = compositionLocalOf<Boolean> {
    error("Bridge connection status is not provided.")
}