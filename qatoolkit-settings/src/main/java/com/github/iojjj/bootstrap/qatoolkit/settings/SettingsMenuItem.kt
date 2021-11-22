package com.github.iojjj.bootstrap.qatoolkit.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable

@Stable
interface SettingsMenuItem {

    @get:Stable
    @get:StringRes
    val title: Int

    @get:Stable
    @get:DrawableRes
    val icon: Int
}