package com.github.iojjj.bootstrap.qatoolkit.ruler

import com.github.iojjj.bootstrap.qatoolkit.R
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.Checkable
import com.github.iojjj.bootstrap.qatoolkit.toolkitbar.ToolkitBarMenuItem

sealed interface RulerModeMenuItem {

    data class ToggleDimensionType(
        override val text: String = "DP",
        override val isEnabled: Boolean = true,
    ) : RulerModeMenuItem,
        ToolkitBarMenuItem.TextOnly

    data class TogglePercentVisibility(
        override val text: String = "%",
        override val icon: Int = R.drawable.qatoolkit_common_ic_visible_24dp,
        override val isEnabled: Boolean = true,
        override val isChecked: Boolean = false,
    ) : RulerModeMenuItem,
        ToolkitBarMenuItem.IconText,
        Checkable

    data class AddRuler(
        override val icon: Int = R.drawable.qatoolkit_ruler_ic_add_24dp,
        override val isEnabled: Boolean = true,
    ) : RulerModeMenuItem,
        ToolkitBarMenuItem.IconOnly

    data class RemoveRuler(
        override val icon: Int = R.drawable.qatoolkit_ruler_ic_remove_24dp,
        override val isEnabled: Boolean = true,
    ) : RulerModeMenuItem,
        ToolkitBarMenuItem.IconOnly

    data class ToggleSettings(
        override val icon: Int = R.drawable.qatoolkit_common_ic_settings_24dp,
        override val isEnabled: Boolean = true,
        override val isChecked: Boolean = false,
    ) : RulerModeMenuItem,
        ToolkitBarMenuItem.IconOnly,
        Checkable
}